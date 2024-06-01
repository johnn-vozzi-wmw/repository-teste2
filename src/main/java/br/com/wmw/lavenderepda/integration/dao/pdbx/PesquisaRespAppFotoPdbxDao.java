package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.PesquisaRespAppFoto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Vector;

public class PesquisaRespAppFotoPdbxDao extends LavendereCrudDbxDao  {

	protected BaseDomain getBaseDomainDefault() {
		return new PesquisaRespAppFoto();
	}

	private static PesquisaRespAppFotoPdbxDao instance;
	public static final String TABLE_NAME = "TBLVPPESQUISARESPAPPFOTO";

	public PesquisaRespAppFotoPdbxDao() {
		super(TABLE_NAME);
	}

	public static PesquisaRespAppFotoPdbxDao getInstance() {
		if (instance == null) {
			instance = new PesquisaRespAppFotoPdbxDao();
		}
		return instance;
	}

	@Override
	protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		PesquisaRespAppFoto pesquisaRespAppFoto = new PesquisaRespAppFoto();
		pesquisaRespAppFoto.rowKey = rs.getString("rowkey");
		pesquisaRespAppFoto.cdEmpresa = rs.getString("cdEmpresa");
		pesquisaRespAppFoto.cdRepresentante = rs.getString("cdRepresentante");
		pesquisaRespAppFoto.cdCliente = rs.getString("cdCliente");
		pesquisaRespAppFoto.cdPesquisaApp = rs.getString("cdPesquisaApp");
		pesquisaRespAppFoto.cdQuestionario = rs.getString("cdQuestionario");
		pesquisaRespAppFoto.cdPergunta = rs.getString("cdPergunta");
		pesquisaRespAppFoto.cdResposta = rs.getString("cdResposta");
        pesquisaRespAppFoto.cdUsuario = rs.getString("cdUsuario");
        pesquisaRespAppFoto.imFoto = rs.getString("imFoto");
		pesquisaRespAppFoto.flEnviadoServidor = rs.getString("flEnviadoServidor");
		pesquisaRespAppFoto.nuCarimbo = rs.getInt("nuCarimbo");
        pesquisaRespAppFoto.flTipoAlteracao = rs.getString("flTipoAlteracao");
		return pesquisaRespAppFoto;
	}

	@Override
	public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
		sql.append(" rowkey,");
		sql.append(" CDEMPRESA,");
		sql.append(" CDREPRESENTANTE,");
		sql.append(" CDCLIENTE,");
		sql.append(" CDPESQUISAAPP,");
		sql.append(" CDQUESTIONARIO,");
		sql.append(" CDPERGUNTA,");
		sql.append(" CDRESPOSTA,");
        sql.append(" CDUSUARIO,");
        sql.append(" IMFOTO,");
        sql.append(" FLENVIADOSERVIDOR,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
	}

	@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPESQUISAAPP,");
        sql.append(" CDQUESTIONARIO,");
        sql.append(" CDPERGUNTA,");
        sql.append(" CDRESPOSTA,");
        sql.append(" CDUSUARIO,");
        sql.append(" IMFOTO,");
        sql.append(" FLENVIADOSERVIDOR,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO");
    }
	
	@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	PesquisaRespAppFoto pesquisaRespAppFoto = (PesquisaRespAppFoto) domain;
        sql.append(Sql.getValue(pesquisaRespAppFoto.cdEmpresa)).append(",");
        sql.append(Sql.getValue(pesquisaRespAppFoto.cdRepresentante)).append(",");
        sql.append(Sql.getValue(pesquisaRespAppFoto.cdCliente)).append(",");
        sql.append(Sql.getValue(pesquisaRespAppFoto.cdPesquisaApp)).append(",");
        sql.append(Sql.getValue(pesquisaRespAppFoto.cdQuestionario)).append(",");
        sql.append(Sql.getValue(pesquisaRespAppFoto.cdPergunta)).append(",");
        sql.append(Sql.getValue(pesquisaRespAppFoto.cdResposta)).append(",");
        sql.append(Sql.getValue(pesquisaRespAppFoto.cdUsuario)).append(",");
        sql.append(Sql.getValue(pesquisaRespAppFoto.imFoto)).append(",");
        sql.append(Sql.getValue(pesquisaRespAppFoto.flEnviadoServidor)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(pesquisaRespAppFoto.flTipoAlteracao));
    }
	
	@Override
	protected void addWhereByExample(BaseDomain baseDomain, StringBuffer sql) throws SQLException {
		PesquisaRespAppFoto pesquisaRespAppFoto = (PesquisaRespAppFoto) baseDomain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", pesquisaRespAppFoto.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", pesquisaRespAppFoto.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("CDCLIENTE", pesquisaRespAppFoto.cdCliente);
		sqlWhereClause.addAndConditionEquals("CDPESQUISAAPP", pesquisaRespAppFoto.cdPesquisaApp);
		sqlWhereClause.addAndConditionEquals("CDQUESTIONARIO", pesquisaRespAppFoto.cdQuestionario);
		sqlWhereClause.addAndConditionEquals("CDPERGUNTA", pesquisaRespAppFoto.cdPergunta);
		sqlWhereClause.addAndConditionEquals("CDRESPOSTA", pesquisaRespAppFoto.cdResposta);
		sqlWhereClause.addAndConditionEquals("IMFOTO", pesquisaRespAppFoto.imFoto);
		sqlWhereClause.addAndConditionNotEquals("CDRESPOSTA", pesquisaRespAppFoto.cdRespostaDiferente);
		sql.append(sqlWhereClause.getSql());
	}

    @Override
	protected void addWhereDeleteByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
		PesquisaRespAppFoto pesquisaRespAppFoto = (PesquisaRespAppFoto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndConditionEquals("CDEMPRESA", pesquisaRespAppFoto.cdEmpresa);
		sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", pesquisaRespAppFoto.cdRepresentante);
		sqlWhereClause.addAndConditionEquals("CDCLIENTE", pesquisaRespAppFoto.cdCliente);
		sqlWhereClause.addAndConditionEquals("CDPESQUISAAPP", pesquisaRespAppFoto.cdPesquisaApp);
		sqlWhereClause.addAndConditionEquals("CDQUESTIONARIO", pesquisaRespAppFoto.cdQuestionario);
		sqlWhereClause.addAndConditionEquals("CDPERGUNTA", pesquisaRespAppFoto.cdPergunta);
		sqlWhereClause.addAndConditionEquals("CDRESPOSTA", pesquisaRespAppFoto.cdResposta);
		sqlWhereClause.addAndConditionEquals("IMFOTO", pesquisaRespAppFoto.imFoto);
		sqlWhereClause.addAndConditionNotEquals("CDRESPOSTA", pesquisaRespAppFoto.cdRespostaDiferente);
		sql.append(sqlWhereClause.getSql());
	}
    
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
		PesquisaRespAppFoto pesquisaRespAppFoto = (PesquisaRespAppFoto) domain;
		sql.append(" FLENVIADOSERVIDOR = ").append(Sql.getValue(pesquisaRespAppFoto.flEnviadoServidor));
	}

	public Vector findAllImagesToSend() throws SQLException {
		StringBuffer sql = getSqlBuffer();
        sql.append(" select * from ");
        sql.append(tableName);
        sql.append(" where flEnviadoServidor != ").append(Sql.getValue(ValueUtil.VALOR_SIM));
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
			Vector pesquisaRespAppFotoList = new Vector();
			while (rs.next()) {
				pesquisaRespAppFotoList.addElement(populate(getBaseDomainDefault(), rs));
			}
			return pesquisaRespAppFotoList;
		}
	}
	
	public Vector findListImFoto(BaseDomain domain) throws SQLException { 
		StringBuffer sql = getSqlBuffer();
        sql.append(" select IMFOTO from ");
        sql.append(tableName);
        addWhereByExample(domain, sql);
        try (Statement st = getCurrentDriver().getStatement(); ResultSet rs = st.executeQuery(sql.toString())) {
        	Vector imFotoList = new Vector();
			while (rs.next()) {
				PesquisaRespAppFoto pesquisaRespAppFoto = new PesquisaRespAppFoto();
				pesquisaRespAppFoto.imFoto = rs.getString("imFoto");
				imFotoList.addElement(pesquisaRespAppFoto);
			}
			return imFotoList; 
		}
	}
	
	public void updateFotosParaEnvio(PesquisaRespAppFoto pesquisaRespAppFoto) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" update ").append(tableName).append(" set");
		sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(pesquisaRespAppFoto.flTipoAlteracao));
		sql.append(" where CDEMPRESA = ").append(Sql.getValue(pesquisaRespAppFoto.cdEmpresa));
		sql.append(" and CDREPRESENTANTE = ").append(Sql.getValue(pesquisaRespAppFoto.cdRepresentante));
		sql.append(" and CDPESQUISAAPP = ").append(Sql.getValue(pesquisaRespAppFoto.cdPesquisaApp));
		sql.append(" and CDCLIENTE = ").append(Sql.getValue(pesquisaRespAppFoto.cdCliente));
		sql.append(" and CDQUESTIONARIO = ").append(Sql.getValue(pesquisaRespAppFoto.cdQuestionario));
		sql.append(" and CDPERGUNTA = ").append(Sql.getValue(pesquisaRespAppFoto.cdPergunta));
		sql.append(" and CDRESPOSTA = ").append(Sql.getValue(pesquisaRespAppFoto.cdResposta));
		sql.append(" and FLENVIADOSERVIDOR != ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		//--
		try {
			executeUpdate(sql.toString());			
		} catch (ApplicationException e) {
			//Significa que apenas não houve alteração de registros, pois a resposta não possui foto
		}
	}
	
}
