package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.integration.dao.CrudDbxDao;

public abstract class LavendereCrudDbxDao extends CrudDbxDao {

    public LavendereCrudDbxDao(String newTableName) {
        super(newTableName);
    }

}