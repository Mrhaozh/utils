  //属性扫描器

       // PropertyDescriptor pd = new PropertyDescriptor("cSysPassword", SysUser.class);
        ObjectMapper mapper = new ObjectMapper();

        Map m = mapper.readValue(mapper.writeValueAsString(obj), Map.class);
        String value=m.get("cSysPassword").toString();
        //从属性描述器中获取 get 方法
        //Method method = pd.getReadMethod();
        //结果
        //Object value = method.invoke(obj);
        //执行方法并返回结果
        return value==null?"":value;
