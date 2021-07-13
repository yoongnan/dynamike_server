/***********************************************************
 * Copyright 2017 VMware, Inc.  All rights reserved.
***********************************************************/

package com.dynamike.pos.util;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class OffsetBasedPageRequest {

    public static Set<Long> toLongIds(String arg) {
        return new HashSet<Long>(Arrays.stream(arg.split(",")).
                map(String::trim).mapToLong(Long::parseLong).boxed().collect(Collectors.toList()));
    }

    public static String toCommaStr(Set<Long> ids) {
        return ids.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    public static String toJsonStr(Object result) throws Exception {
        return new ObjectMapper().writeValueAsString(result);
    }

    public static JsonNode objToJsonNode(Object result) throws Exception {
        return new ObjectMapper().convertValue(result, JsonNode.class);
    }

    public static JsonNode toJsonNode(String str) throws Exception {
        return new ObjectMapper().readValue(str, JsonNode.class);
    }

    public static JsonNode updateJsonNode(JsonNode node, String parent, String value) throws Exception {
        final List<String> EXCLUDED_NODES = Arrays.asList("tags", "references", "certcomponents");
        final List<String> CLEANUP_KEYS = Arrays.asList("id", "solution_id");

        if (node == null) {
            return node;
        }
        if (!node.isArray()) {
            ObjectNode pnode = (ObjectNode)node;
            Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> field = iterator.next();
                final JsonNode subnode = field.getValue();
                if (subnode.isArray()) {
                    for(JsonNode child : subnode) {
                        updateJsonNode(child, field.getKey(), value);
                    }
                }
                else {
                    if (CLEANUP_KEYS.contains(field.getKey()) && !EXCLUDED_NODES.contains(parent)) {
                        pnode.put(field.getKey(), value);
                    }
                }
            }
            
        }
        else {
            for(JsonNode child : node) {
                updateJsonNode(child, parent, value);
            }
        }
        return node;
    }

    public static JsonNode toExportData(Object obj, String groupname, ObjectNode root) throws Exception {
        JsonNode node = updateJsonNode(OffsetBasedPageRequest.objToJsonNode(obj), null, null);
        if (node != null && groupname != null) {
            root = root != null ? root : new ObjectMapper().createObjectNode();
            root.set(groupname, node);
            return root;
        }
        return node;
    }
    
    public static JsonNode toJsonNodeFromFile(MultipartFile file) throws Exception {
        String content = file != null ? new String(file.getBytes()) : null;
        JsonNode jsonnode = content != null ? OffsetBasedPageRequest.toJsonNode(content) : null;
        if (jsonnode == null) {
            throw new Exception("failed to parse a file - the json format is required");
        }
        return jsonnode;
    }
    
    public static <T> List<T> getSubList(JsonNode node, String key, Class<T> classtype) throws Exception {
        List<T> list = new ArrayList<T>();
        try {
            JsonNode dnode = node != null ? node.get(key) : null;
            if (dnode != null) {
                if (dnode.isArray()) {
                    for(JsonNode child : dnode) {
                        T data = OffsetBasedPageRequest.toClassObj(child, classtype);
                        if(data != null) {
                            list.add(data);
                        }
                    }
                }
                else {
                    T data = OffsetBasedPageRequest.toClassObj(dnode, classtype);
                    if(data != null) {
                        list.add(data);
                    }
                }                
            }
        }
        catch(Exception e) {
            throw new Exception("Invalid Json data found " + e.getMessage());
        }
        return list;
    }

    public static <T> T toClassObj(Object type, Class<T> classtype) {
        if (classtype == null || type == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(type, classtype);
    }


    public static <T> List<T> toClassObjList(List<Object> list, Class<T> classtype) {
        if (classtype == null || list == null || list.size() == 0) {
            return new ArrayList<T>();
        }
        List<T> converteditems = new ArrayList<T>();
        for (Object item : list) {
            converteditems.add(toClassObj(item, classtype));
        }
        return converteditems;
    }
   

    public static <T> T strToClassObj(String str, Class<T> classtype) throws Exception {
        return new ObjectMapper().readValue(str, classtype);
    }
    
    public static<T> List<T> strToClassObjList(String str, Class<T> classtype) throws Exception {
        JsonNode nodes = toJsonNode(str);
        List<T> list = new ArrayList<T>();
        if (nodes.isArray()) {
            for(JsonNode node : nodes) {
                list.add(new ObjectMapper().readValue(toJsonStr(node), classtype));
            }
        }
        else {
            list.add(new ObjectMapper().readValue(toJsonStr(nodes), classtype));            
        }
        return list;
    }

    public static String getChecksum(Object object) throws Exception {
        ByteArrayOutputStream bytestream = null;
        ObjectOutputStream objstream = null;
        try {
            bytestream = new ByteArrayOutputStream();
            objstream = new ObjectOutputStream(bytestream);
            objstream.writeObject(object);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytestream.toByteArray());
            return DatatypeConverter.printHexBinary(thedigest);
        } finally {
            IOUtils.closeQuietly(objstream);
            IOUtils.closeQuietly(bytestream);
        }
    }

    public static List<Map<String, Object>> toObjectMapList(List<Map<?, ?>> sources) {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        if (sources != null && sources.size() > 0) {
            for(Map<?, ?> obj: sources) {
                Map<String, Object> values = new HashMap<String, Object>();
                for(Object key : obj.keySet()) {
                    Object value = obj.get(key);
                    values.put(key.toString(), value);
                }
                results.add(values);
            }
        }
        return results;
    }
}

