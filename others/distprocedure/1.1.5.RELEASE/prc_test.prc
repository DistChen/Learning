create or replace procedure prc_test(
       p_str in varchar2,
       p_num in number,
       p_strDate in date,
       p_date in date,
       p_inclob in clob,
       p_inblob in blob,
       p_info out varchar2,
       p_outclob out clob,
       p_outblob out blob,
       p_cursor out sys_refcursor
)is
begin
  p_info :='字符串为：'||p_str||',数字为：'||p_num||',日期为：'||to_char(p_strDate,'yyyy-mm-dd')||','||to_char(p_date,'yyyy-mm-dd')||',Clob长度为：'||length(p_inclob);
  select p_inblob into p_outblob from dual;
  select 'clob->hello world' into p_outclob from dual;
  open p_cursor for select * from(
       select 1 id,sysdate age,'CUG' school, '张三' name,'11' other from dual
       union
       select 2 id,sysdate age,'HUST' school, '李四' name,'22' other  from dual
  );
end prc_test;
