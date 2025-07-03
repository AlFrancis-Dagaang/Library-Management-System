package com.app.service;

import com.app.dao.LibrarianDAO;
import com.app.model.Admin;

public class LibrarianService {
    private LibrarianDAO librarianDAO;
    public LibrarianService() {}
    public LibrarianService(LibrarianDAO librarianDAO) {
        this.librarianDAO = librarianDAO;
    }

    public boolean loginAsAdmin(Admin admin) {
        Admin tempAdmin = this.librarianDAO.getAdminByUsername(admin.getUsername());

        if(tempAdmin != null && tempAdmin.getPassword().equals(admin.getPassword())) {
            return true;
        }
        return false;
    }
}
