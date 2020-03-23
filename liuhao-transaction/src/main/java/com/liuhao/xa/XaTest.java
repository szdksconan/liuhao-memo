package com.liuhao.xa;

import com.mysql.cj.jdbc.JdbcConnection;
import com.mysql.cj.jdbc.MysqlXAConnection;
import com.mysql.cj.jdbc.MysqlXid;

import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class XaTest {


    public static void main(String[] args) throws SQLException, XAException {

        boolean xalog = true;
       Connection connection_1 =  DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/db_1","root","123456");
       Connection connection_2 =  DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/db_2","root","123456");


       //xa事务管理其连接 也就是TM
        XAConnection xaConnection_1 = new MysqlXAConnection((JdbcConnection) connection_1,xalog);
        XAConnection xaConnection_2 = new MysqlXAConnection((JdbcConnection) connection_2,xalog);

        //获取XA资源 也就是RM

        XAResource xaResource_1 = xaConnection_1.getXAResource();
        XAResource xaResource_2 = xaConnection_2.getXAResource();

        //全局事务ID
        byte[] bytes_id = "g000001".getBytes();
        int format = 1;

        //con_1事务ID
        byte[] bytes_id_1 = "o000001".getBytes();
        byte[] bytes_id_2 = "o000002".getBytes();

        Xid xid_1 = new MysqlXid(bytes_id,bytes_id_1,1);
        Xid xid_2 = new MysqlXid(bytes_id,bytes_id_2,1);

        //执行分支上的事务
        xaResource_1.start(xid_1,XAResource.TMNOFLAGS);
        //预提交
        PreparedStatement preparedStatement_1 = connection_1.prepareStatement("insert into a (name) value('liuhao')");
        preparedStatement_1.execute();
        xaResource_1.end(xid_1,XAResource.TMSUCCESS);


        xaResource_2.start(xid_2,XAResource.TMNOFLAGS);
        PreparedStatement preparedStatement_2 = connection_2.prepareStatement("insert into b (name) value('liqian')");
        preparedStatement_2.execute();
        xaResource_2.end(xid_2,XAResource.TMSUCCESS);


        //===二段提交====

        //询问所有的所有的RM 所有的提交状况
        int status_1 = xaResource_1.prepare(xid_1);
        int status_2 = xaResource_2.prepare(xid_2);


        if (status_1 == XAResource.XA_OK && status_2 == XAResource.XA_OK){
            xaResource_1.commit(xid_1,false);//false表示为 非一段提交
            xaResource_2.commit(xid_2,false);//false表示为 非一段提交
        } else{
            xaResource_1.rollback(xid_1);
            xaResource_1.rollback(xid_2);
        }

        preparedStatement_1.close();
        preparedStatement_2.close();
        connection_1.close();
        connection_2.close();


    }
}
