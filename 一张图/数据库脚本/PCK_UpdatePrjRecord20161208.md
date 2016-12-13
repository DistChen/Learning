### 修改审批项目Mark值

> Author  : yangmin
> Created : 2016/12/8 14:12:33
> Purpose : 规划审批库中，有些项目已经有了红线，但是该项目在SYS_DIC_PRJSTAGERECORD表中的Mark还是为1，
>           导致前端显示的时候提示“暂未画红线”，现将这部分项目的Mark值更改为3，其他项目的Mark值不变

```
create or replace package PCK_UpdatePrjRecord is

  -- Author  : yangmin
  -- Created : 2016/12/8 14:12:33
  -- Purpose :规划审批库中，有些项目已经有了红线，但是该项目在SYS_DIC_PRJSTAGERECORD表中的Mark还是为1，
  --         导致前端显示的时候提示“暂未画红线”，现将这部分项目的Mark值更改为3，其他项目的Mark值不变
  v_sql clob;
  PROCEDURE PRC_UpdatePrjRecordMark(P_Result OUT varchar2);
  PROCEDURE PRC_UpdatePrjRecordMark2(P_Result OUT varchar2);

end PCK_UpdatePrjRecord;
/
create or replace package body PCK_UpdatePrjRecord is

  -- Author  : yangmin
  -- Created : 2016/12/8 14:12:33
  -- Purpose :规划审批库中，有些项目已经有了红线，但是该项目在SYS_DIC_PRJSTAGERECORD表中的Mark还是为1，
  --         导致前端显示的时候提示“暂未画红线”，现将这部分项目的Mark值更改为3，其他项目的Mark值不变
  --         
 
  PROCEDURE PRC_UpdatePrjRecordMark(P_Result OUT varchar2)
  is
      TYPE CUR_DATA_TYPE1 IS RECORD
      (
         businessname sys_dic_prjstagemap.businessname % type,
         phylayername sys_dic_projectstage.phylayername % type
      );
      curLayer CUR_DATA_TYPE1;
      TYPE CUR_DATA_TYPE2 IS RECORD
      (
         xmbh sys_dic_prjstagerecord.xmbh % type,
         slbh sys_dic_prjstagerecord.slbh % type,
         status sys_dic_prjstagerecord.slbh % type
      );
      curRecord CUR_DATA_TYPE2;
      TYPE ref_cursor_type IS REF CURSOR;  --定义一个动态游标
      cursor1 ref_cursor_type;--cusor1存放业务和图层对应的关系表
      cursor2 ref_cursor_type;--cusor2存放上一个cusor1的一条图层记录查询到的xmbh，slbh是否在图层中有数据，有：标示为1，无：标示为0
  begin
      --cusor1存放业务和图层对应的关系表
      v_sql:='select t1.businessname,t2.phylayername from sys_dic_prjstagemap t1,sys_dic_projectstage t2
             where t1.prjtypeid=t2.ref_projecttype_id and t1.stagenameid = t2.ref_stagetype_id';

      open cursor1 for v_sql;
      loop
      fetch cursor1 into curLayer;
      exit when cursor1%notfound;
        --内嵌一个cusor2存放上一个cusor1的一条图层记录查询到的xmbh，slbh是否在图层中有数据，有：标示为1，无：标示为0
        v_sql:='select t1.xmbh,t1.slbh,
               case when t2.slbh is not null then ''1''
                    when t2.slbh is null then ''0''
                    end status
               from sys_dic_prjstagerecord t1,'||curLayer.phylayername||' t2
               where t1.xmbh = t2.xmbh and t1.slbh = t2.slbh';
               --dbms_output.put_line(v_sql);
        open cursor2 for v_sql;
        loop
        fetch cursor2 into curRecord;
        exit when cursor2%notfound;
        --遍历cusor1中的一条记录进行对sys_dic_prjstagerecord中的相应记录进行mark值的修改
        if curRecord.status = '1'
        then
           update sys_dic_prjstagerecord t set t.mark = '3' where t.xmbh = curRecord.xmbh and t.slbh = curRecord.slbh;
        end if;
        end loop;
      end loop;
      commit;            
      select 'succuss' into P_Result from dual;

      EXCEPTION
      WHEN NO_DATA_FOUND THEN
        select 'null' into P_Result from dual;
      WHEN TOO_MANY_ROWS THEN
        select 'error' into P_Result from dual;
      WHEN OTHERS THEN
        select 'fail' into P_Result from dual;

  end PRC_UpdatePrjRecordMark;
  
  
  PROCEDURE PRC_UpdatePrjRecordMark2(P_Result OUT varchar2)
  is
      TYPE CUR_DATA_TYPE1 IS RECORD
      (
         businessid   sys_dic_prjstagemap.businessid % type,  
         businessname sys_dic_prjstagemap.businessname % type,
         phylayername sys_dic_projectstage.phylayername % type
      );
      curLayer CUR_DATA_TYPE1;
      TYPE ref_cursor_type IS REF CURSOR;  --定义一个动态游标
      cursor1 ref_cursor_type;--cusor1存放业务和图层对应的关系表
   begin
      --cusor1存放业务和图层对应的关系表
      v_sql:='select t1.businessid,t1.businessname,t2.phylayername from sys_dic_prjstagemap t1,sys_dic_projectstage t2
             where t1.prjtypeid=t2.ref_projecttype_id and t1.stagenameid = t2.ref_stagetype_id';

      open cursor1 for v_sql;
      loop
      fetch cursor1 into curLayer;
      exit when cursor1%notfound;
      
      v_sql:='update sys_dic_prjstagerecord t 
              set t.mark=3
              where exists( select 1 from '||curLayer.phylayername||' p 
              where t.stageid='||curLayer.businessid||' and t.mark=1 and t.xmbh=p.xmbh and t.slbh=p.slbh)';
      EXECUTE IMMEDIATE v_sql;
      end loop;
      commit;      
      select 'succuss' into P_Result from dual;

      EXCEPTION
      WHEN NO_DATA_FOUND THEN
        select 'null' into P_Result from dual;
      WHEN TOO_MANY_ROWS THEN
        select 'error' into P_Result from dual;
      WHEN OTHERS THEN
        select 'fail' into P_Result from dual;

  end PRC_UpdatePrjRecordMark2;

end PCK_UpdatePrjRecord;
/

```