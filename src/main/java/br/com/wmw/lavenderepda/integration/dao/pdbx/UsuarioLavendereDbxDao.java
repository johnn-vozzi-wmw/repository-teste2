package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Usuario;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.UsuarioDbxDao;
import br.com.wmw.lavenderepda.business.domain.UsuarioLavendere;

public class UsuarioLavendereDbxDao extends UsuarioDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new UsuarioLavendere();
	}
	
	public UsuarioLavendereDbxDao() {
        super(UsuarioLavendere.TABLE_NAME);
    }
	
	public static UsuarioDbxDao getInstance() {
        if (instance == null) {
			instance = new UsuarioLavendereDbxDao();
        }
        return instance;
    }
	
    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
    	super.addInsertColumns(sql);
        sql.append(" ,CDFUNCAO");
    }
    
    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	super.addInsertValues(domain, sql);
    	Usuario usuario = (Usuario)domain;
    	sql.append(",");
        sql.append(Sql.getValue(usuario.cdFuncao));
    }

	
	/*@Override
	protected BaseDomain populate(ResultSet rs) throws java.sql.SQLException {
		UsuarioLavendere usuario = new UsuarioLavendere();
        usuario.rowKey = rs.getString("rowkey");
        usuario.cdUsuario = rs.getString("cdUsuario");
        usuario.nmUsuario = rs.getString("nmUsuario");
        usuario.dsLogin = rs.getString("dsLogin");
        usuario.dsSenha = rs.getString("dsSenha");
        usuario.dsEmail = rs.getString("dsEmail");
        usuario.imFoto = rs.getString("imFoto");
        usuario.cdFuncao = rs.getString("cdFuncao");
        usuario.nuCarimbo = rs.getInt("nuCarimbo");
        usuario.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return usuario;
		//return super.populate(rs);
	}*/
	
}
