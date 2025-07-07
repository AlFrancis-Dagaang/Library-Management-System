package com.app.controller;

import com.app.config.AppConfig;
import com.app.exception.ResourceNotFound;
import com.app.model.Bill;
import com.app.model.Transaction;
import com.app.service.BillService;
import com.app.util.JsonUtil;
import com.app.util.PathUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@WebServlet("/v1/lms/bills/*")
public class BillServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BillService billService;

    @Override
    public void init(){
        this.billService = AppConfig.getBillService();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String [] paths = PathUtil.getPaths(path);

        try{
            if(paths.length ==3 && PathUtil.isNumeric(paths[1]) && paths[2].equals("fines")){ //----------- /{id}/fines
                int billId = Integer.parseInt(paths[1]);
                Bill bill = this.billService.payBill(billId);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, bill);
            }

        }catch (IllegalArgumentException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }catch (ResourceNotFound e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
        catch (RuntimeException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String [] paths = PathUtil.getPaths(path);

        try{
            if(path == null){//-------------- > "get all bills"
                List<Bill> bills = this.billService.getAllBills();
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, bills);
            }else if(paths.length ==2 && PathUtil.isNumeric(paths[1])){ //----------- > /{id} "Get bills by id"
                int billId = Integer.parseInt(paths[1]);
                Bill bill = this.billService.getBillById(billId);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, bill);
            }

        }catch (ResourceNotFound e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }catch (RuntimeException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String [] paths = PathUtil.getPaths(path);

        try{
            if(paths.length ==2 && PathUtil.isNumeric(paths[1])){
                int billId = Integer.parseInt(paths[1]);
                this.billService.deleteBill(billId);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, "Successfully deleted");
            }

        }catch (ResourceNotFound e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (RuntimeException e) {
            JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
