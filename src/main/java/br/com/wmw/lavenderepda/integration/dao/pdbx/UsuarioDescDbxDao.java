package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.UsuarioDesc;
import totalcross.sql.ResultSet;

public class UsuarioDescDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new UsuarioDesc();
	}

    private static UsuarioDescDbxDao instance;

    public UsuarioDescDbxDao() {
        super(UsuarioDesc.TABLE_NAME); 
    }
    
    public static UsuarioDescDbxDao getInstance() {
        if (instance == null) {
            instance = new UsuarioDescDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        UsuarioDesc usuariodesc = new UsuarioDesc();
        usuariodesc.rowKey = rs.getString("rowkey");
        usuariodesc.flOrigemDesconto = rs.getString("flOrigemDesconto");
        usuariodesc.cdUsuario = rs.getString("cdUsuario");
        usuariodesc.vlTotalFaturado = ValueUtil.round(rs.getDouble("vlTotalFaturado"));
        usuariodesc.vlTotalDesconto = ValueUtil.round(rs.getDouble("vlTotalDesconto"));
        usuariodesc.vlPctMaxDesconto = ValueUtil.round(rs.getDouble("vlPctMaxDesconto"));
        usuariodesc.vlPctMaxDescPonderado = ValueUtil.round(rs.getDouble("vlPctMaxDescPonderado"));
        usuariodesc.vlPctMaxDescUsu = ValueUtil.round(rs.getDouble("vlPctMaxDescUsu"));
        usuariodesc.nuOrdemLiberacao = rs.getInt("nuOrdemLiberacao");
        usuariodesc.nuCarimbo = rs.getInt("nuCarimbo");
        usuariodesc.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return usuariodesc;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" FLORIGEMDESCONTO,");
        sql.append(" CDUSUARIO,");
        sql.append(" VLTOTALFATURADO,");
        sql.append(" VLTOTALDESCONTO,");
        sql.append(" VLPCTMAXDESCONTO,");
        sql.append(" VLPCTMAXDESCPONDERADO,");
        sql.append(" VLPCTMAXDESCUSU,");
        sql.append(" NUORDEMLIBERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" FLORIGEMDESCONTO,");
        sql.append(" CDUSUARIO,");
        sql.append(" VLTOTALFATURADO,");
        sql.append(" VLTOTALDESCONTO,");
        sql.append(" VLPCTMAXDESCONTO,");
        sql.append(" VLPCTMAXDESCPONDERADO,");
        sql.append(" VLPCTMAXDESCUSU,");
        sql.append(" NUORDEMLIBERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        UsuarioDesc usuarioDesc = (UsuarioDesc) domain;
        sql.append(Sql.getValue(usuarioDesc.flOrigemDesconto)).append(",");
        sql.append(Sql.getValue(usuarioDesc.cdUsuario)).append(",");
        sql.append(Sql.getValue(usuarioDesc.vlTotalFaturado)).append(",");
        sql.append(Sql.getValue(usuarioDesc.vlTotalDesconto)).append(",");
        sql.append(Sql.getValue(usuarioDesc.vlPctMaxDesconto)).append(",");
        sql.append(Sql.getValue(usuarioDesc.vlPctMaxDescPonderado)).append(",");
        sql.append(Sql.getValue(usuarioDesc.vlPctMaxDescUsu)).append(",");
        sql.append(Sql.getValue(usuarioDesc.nuOrdemLiberacao)).append(",");
        sql.append(Sql.getValue(usuarioDesc.nuCarimbo)).append(",");
        sql.append(Sql.getValue(usuarioDesc.flTipoAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        UsuarioDesc usuarioDesc = (UsuarioDesc) domain;
        sql.append(" VLTOTALFATURADO = ").append(Sql.getValue(usuarioDesc.vlTotalFaturado)).append(",");
        sql.append(" VLTOTALDESCONTO = ").append(Sql.getValue(usuarioDesc.vlTotalDesconto)).append(",");
        sql.append(" VLPCTMAXDESCONTO = ").append(Sql.getValue(usuarioDesc.vlPctMaxDesconto)).append(",");
        sql.append(" VLPCTMAXDESCPONDERADO = ").append(Sql.getValue(usuarioDesc.vlPctMaxDescPonderado)).append(",");
        sql.append(" VLPCTMAXDESCUSU = ").append(Sql.getValue(usuarioDesc.vlPctMaxDescUsu)).append(",");
        sql.append(" NUORDEMLIBERACAO = ").append(Sql.getValue(usuarioDesc.nuOrdemLiberacao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(usuarioDesc.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(usuarioDesc.flTipoAlteracao));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        UsuarioDesc usuariodesc = (UsuarioDesc) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("FLORIGEMDESCONTO = ", usuariodesc.flOrigemDesconto);
		sqlWhereClause.addAndCondition("CDUSUARIO = ", usuariodesc.cdUsuario);
		sqlWhereClause.addAndCondition("VLPCTINICIOALCADA <=", usuariodesc.vlPctFimAlcada);
		sqlWhereClause.addAndCondition("VLPCTFIMALCADA >=", usuariodesc.vlPctFimAlcada);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public boolean isUsuarioLiberacaoAlcada(UsuarioDesc usuarioDesc) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("SELECT count(*) FROM TBLVPUSUARIODESC ");
    	addWhereByExample(usuarioDesc, sql);
    	return getInt(sql.toString()) > 0;
    }
    
}