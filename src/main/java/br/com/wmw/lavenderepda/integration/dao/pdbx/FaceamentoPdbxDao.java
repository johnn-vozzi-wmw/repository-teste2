package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Faceamento;
import totalcross.sql.ResultSet;
import totalcross.util.Vector;

public class FaceamentoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Faceamento();
	}

    private static FaceamentoPdbxDao instance;

    public FaceamentoPdbxDao() {
        super(Faceamento.TABLE_NAME);
    }

    public static FaceamentoPdbxDao getInstance() {
        if (instance == null) {
            instance = new FaceamentoPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Faceamento faceamento = new Faceamento();
        faceamento.rowKey = rs.getString("rowkey");
        faceamento.cdEmpresa = rs.getString("cdEmpresa");
        faceamento.cdRepresentante = rs.getString("cdRepresentante");
        faceamento.cdCliente = rs.getString("cdCliente");
        faceamento.cdProduto = rs.getString("cdProduto");
        faceamento.qtPontoEquilibrio = ValueUtil.round(rs.getDouble("qtPontoequilibrio"));
        faceamento.cdUsuario = rs.getString("cdUsuario");
        faceamento.flTipoAlteracao = rs.getString("flTipoAlteracao");
        return faceamento;
    }

	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		Faceamento faceamento = new Faceamento();
		faceamento.rowKey = rs.getString("rowkey");
		faceamento.cdEmpresa = rs.getString("cdEmpresa");
		faceamento.cdRepresentante = rs.getString("cdRepresentante");
		faceamento.cdCliente = rs.getString("cdCliente");
		faceamento.cdProduto = rs.getString("cdProduto");
		faceamento.qtPontoEquilibrio = ValueUtil.round(rs.getDouble("qtPontoequilibrio"));
		faceamento.cdUsuario = rs.getString("cdUsuario");
		faceamento.flTipoAlteracao = rs.getString("flTipoAlteracao");
		return faceamento;
	}

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" QTPONTOEQUILIBRIO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
		sql.append(" tb.rowkey,");
		sql.append(" tb.CDEMPRESA,");
		sql.append(" tb.CDREPRESENTANTE,");
		sql.append(" tb.CDCLIENTE,");
		sql.append(" tb.CDPRODUTO,");
		sql.append(" tb.QTPONTOEQUILIBRIO,");
		sql.append(" tb.CDUSUARIO,");
		sql.append(" tb.FLTIPOALTERACAO");
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" QTPONTOEQUILIBRIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLTIPOALTERACAO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Faceamento faceamento = (Faceamento) domain;
        sql.append(Sql.getValue(faceamento.cdEmpresa)).append(",");
        sql.append(Sql.getValue(faceamento.cdRepresentante)).append(",");
        sql.append(Sql.getValue(faceamento.cdCliente)).append(",");
        sql.append(Sql.getValue(faceamento.cdProduto)).append(",");
        sql.append(Sql.getValue(faceamento.qtPontoEquilibrio)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(faceamento.cdUsuario)).append(",");
        sql.append(Sql.getValue(faceamento.flTipoAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Faceamento faceamento = (Faceamento) domain;
        sql.append(" QTPONTOEQUILIBRIO = ").append(Sql.getValue(faceamento.qtPontoEquilibrio)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(faceamento.flTipoAlteracao));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Faceamento faceamento = (Faceamento) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", faceamento.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", faceamento.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", faceamento.cdCliente);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", faceamento.cdProduto);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    public Vector findAllByExampleSummary(BaseDomain domain) throws java.sql.SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append(" select ");
        addSelectSummaryColumns(domain, sql);
        sql.append(" from ");
        sql.append(tableName);
        sql.append(" tb ");
        addWhereByExampleWithJoin(domain, sql);
        addOrderBy(sql, domain);
        return findAllSummary(domain, sql.toString());
    }

    protected void addWhereByExampleWithJoin(BaseDomain domain, StringBuffer sql) {
        Faceamento faceamento = (Faceamento) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addJoin(", TBLVPPRODUTO p where tb.cdProduto = p.cdProduto and  tb.cdEmpresa = p.cdEmpresa and " + Sql.getValue(SessionLavenderePda.usuarioPdaRep.cdRepresentante) + " = p.cdRepresentante");
		sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", faceamento.cdEmpresa);
		sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", faceamento.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.CDCLIENTE = ", faceamento.cdCliente);
		sqlWhereClause.addAndCondition("tb.CDPRODUTO = ", faceamento.cdProduto);
		sql.append(sqlWhereClause.getSql());
    }
	
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
		if ((domain != null) && !ValueUtil.isEmpty(domain.sortAtributte)) {
			boolean dsProduto = domain.sortAtributte.equalsIgnoreCase("DSPRODUTO");
			sql.append(" order by ");
			String[] sortAtributtes = StringUtil.split(domain.sortAtributte, ',');
			String[] sortAsc = StringUtil.split(domain.sortAsc, ',');
			for (int i = 0; i < sortAtributtes.length; i++) {
				sql.append(!dsProduto ? "tb." + sortAtributtes[i] : "p." + sortAtributtes[i]);
				sql.append(ValueUtil.VALOR_SIM.equals(sortAsc[i]) ? " ASC" : " DESC");
				if (!(i == (sortAtributtes.length - 1))) {
					sql.append(" , ");
				}
			}
		}
	}
}