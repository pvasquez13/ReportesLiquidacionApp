/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.globokas.dao;

import com.globokas.bean.LiquidacionBean;
import com.globokas.utils.SqlConection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pvasquez
 */
public class sqlDao {

    public List<LiquidacionBean> getLiquidacion(String fecha, String empresa) {

        List<LiquidacionBean> liquidacionList = new ArrayList<>();

        ResultSet rs = null;
        Connection conn = null;

        try {

            SqlConection c = new SqlConection();
            conn = c.SQLServerConnection();
            PreparedStatement ps;
            ps = conn.prepareStatement("{call uspLiquidacionEmpresa(?,?)}");
            ps.setObject(1, fecha);
            ps.setObject(2, empresa);
            rs = ps.executeQuery();
            while (rs.next()) {
                LiquidacionBean liq = new LiquidacionBean();
                liq.setFecha(rs.getString("FECHA"));
                liq.setCuenta(rs.getString("CUENTA"));
                liq.setCantidad(rs.getInt("CANTIDAD"));
                liq.setImporte(rs.getDouble("IMPORTE"));
                liquidacionList.add(liq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return liquidacionList;

    }

}
