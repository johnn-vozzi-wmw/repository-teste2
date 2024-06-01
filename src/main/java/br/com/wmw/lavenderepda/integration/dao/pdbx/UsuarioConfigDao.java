package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.UsuarioConfig;
import totalcross.sql.ResultSet;

public class UsuarioConfigDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new UsuarioConfig();
	}

    private static UsuarioConfigDao instance;

    public UsuarioConfigDao() {
        super(UsuarioConfig.TABLE_NAME);
    }
    
    public static UsuarioConfigDao getInstance() {
        if (instance == null) {
            instance = new UsuarioConfigDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        UsuarioConfig usuarioConfig = new UsuarioConfig();
        usuarioConfig.rowKey = rs.getString("rowkey");
        usuarioConfig.cdUsuario = rs.getString("cdUsuario");
        usuarioConfig.cdRepresentantePadrao = rs.getString("cdRepresentantePadrao");
        usuarioConfig.nuRamal = rs.getString("nuRamal");
        usuarioConfig.vlPctDescLiberacaoPedido = ValueUtil.round(rs.getDouble("vlPctDescLiberacaoPedido"));
        usuarioConfig.vlPctAcrescLiberacaoPedido = ValueUtil.round(rs.getDouble("vlPctAcrescLiberacaoPedido"));
        usuarioConfig.flLiberaItemPendente = rs.getString("flLiberaItemPendente");
        usuarioConfig.flLiberaOutraOrdem = rs.getString("flLiberaOutraOrdem");
        usuarioConfig.flTipoAlteracao =  rs.getString("flTipoAlteracao");
        usuarioConfig.nuCarimbo = rs.getInt("nuCarimbo");
        return usuarioConfig;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDUSUARIO,");
        sql.append(" CDREPRESENTANTEPADRAO,");
        sql.append(" NURAMAL,");
        sql.append(" VLPCTDESCLIBERACAOPEDIDO,");
        sql.append(" VLPCTACRESCLIBERACAOPEDIDO,");
        sql.append(" FLLIBERAITEMPENDENTE,");
        sql.append(" FLLIBERAOUTRAORDEM,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { }
    
}