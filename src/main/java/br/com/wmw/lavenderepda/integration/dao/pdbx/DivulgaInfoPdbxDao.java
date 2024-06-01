package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.DivulgaInfo;
import totalcross.sql.ResultSet;
import totalcross.util.Date;
import totalcross.util.Vector;

public class DivulgaInfoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new DivulgaInfo();
	}

    private static DivulgaInfoPdbxDao instance;

    public DivulgaInfoPdbxDao() {
        super(DivulgaInfo.TABLE_NAME);
    }

    public static DivulgaInfoPdbxDao getInstance() {
    	return instance == null ? instance = new DivulgaInfoPdbxDao() : instance;
    }
    
    protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {}
    protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException { return null; }
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {}
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {}
    
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	DivulgaInfo divulgaInfo = (DivulgaInfo) domain;
    	sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(divulgaInfo.flTipoAlteracao));
    }

    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowKey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDDIVULGAINFO,");
        sql.append(" DSDIVULGAINFO,");
        sql.append(" FLTIPODIVULGAINFO,");
        sql.append(" DSTIPODIVULGAINFOTXT,");
        sql.append(" NMIMAGEM,");
        sql.append(" NMURL,");
        sql.append(" NMURLIMAGEMLINK,");
        sql.append(" DTINICIAL,");
        sql.append(" DTFINAL");
    }

    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        DivulgaInfo divulgaInfo = new DivulgaInfo();
        divulgaInfo.rowKey = rs.getString("rowkey");
        divulgaInfo.cdEmpresa = rs.getString("cdEmpresa");
        divulgaInfo.cdRepresentante = rs.getString("cdRepresentante");
        divulgaInfo.cdCliente = rs.getString("cdCliente");
        divulgaInfo.cdDivulgaInfo = rs.getString("cdDivulgaInfo");
        divulgaInfo.dsDivulgaInfo = rs.getString("dsDivulgaInfo");
        divulgaInfo.flTipoDivulgaInfo = rs.getString("flTipoDivulgaInfo");
        divulgaInfo.dsTipoDivulgaInfoTxt = rs.getString("dsTipoDivulgaInfoTxt");
        divulgaInfo.nmImagem = rs.getString("nmImagem");
        divulgaInfo.nmUrl = rs.getString("nmurl");
        divulgaInfo.nmUrlImagemLink = rs.getString("nmUrlImagemLink");
        divulgaInfo.dtInicial = rs.getDate("dtInicial");
        divulgaInfo.dtFinal = rs.getDate("dtFinal");
        return divulgaInfo;
    }
    
    protected void addWhereByExample(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        DivulgaInfo divulgaInfoFilter = (DivulgaInfo) domainFilter;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("CDEMPRESA = ", divulgaInfoFilter.cdEmpresa);
        sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", divulgaInfoFilter.cdRepresentante);
        sqlWhereClause.addAndConditionIn(" CDCLIENTE " , divulgaInfoFilter.cdClienteInFilter);
        sqlWhereClause.addAndCondition("CDDIVULGAINFO = ", divulgaInfoFilter.cdDivulgaInfo);
        sqlWhereClause.addAndCondition("FLTIPODIVULGAINFO = ", divulgaInfoFilter.flTipoDivulgaInfo);
        sqlWhereClause.addAndCondition("DTINICIAL <= ", divulgaInfoFilter.dtInicial);
        sqlWhereClause.addAndCondition("DTFINAL >= ", divulgaInfoFilter.dtFinal);
        sqlWhereClause.addAndCondition("FLTIPOALTERACAO = ", divulgaInfoFilter.flTipoAlteracao);
        sql.append(sqlWhereClause.getSql());
        sql.append(" AND ((FLTIPODIVULGAINFO = ").append(Sql.getValue(DivulgaInfo.TIPO_DIVULGA_TEXTO)).append(" AND (DSTIPODIVULGAINFOTXT IS NOT NULL AND DSTIPODIVULGAINFOTXT <> '')) " +
                " OR (FLTIPODIVULGAINFO = ").append(Sql.getValue(DivulgaInfo.TIPO_DIVULGA_IMAGEM)).append(" AND (NMIMAGEM IS NOT NULL AND NMIMAGEM <> ''))" +
                " OR (FLTIPODIVULGAINFO = ").append(Sql.getValue(DivulgaInfo.TIPO_DIVULGA_LINK)).append(" AND (NMURLIMAGEMLINK IS NOT NULL AND NMURLIMAGEMLINK <> '')))");
    }
    
    @Override
    protected void addOrderBy(StringBuffer sql, BaseDomain domain) {
    	sql.append(" ORDER BY CDDIVULGAINFO ASC");
    }

    public Vector findAllNaoAlteradosToDownloadFotos() throws SQLException {
    	final Date dataAtual = DateUtil.getCurrentDate();
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select ");
    	addSelectColumns(null, sql);
    	sql.append(" from ")
	       .append(tableName)
	       .append(" tb ")
	       .append(" where cdEmpresa = ")
	       .append(Sql.getValue(SessionLavenderePda.cdEmpresa))
	       .append(" and cdRepresentante = ")
	       .append(Sql.getValue(SessionLavenderePda.getRepresentante().cdRepresentante))
	       .append(" and  (fltipoalteracao = ")
	       .append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL))
	       .append(" or fltipoalteracao is null)")
	       .append(" and FLTIPODIVULGAINFO = ").append(Sql.getValue(DivulgaInfo.TIPO_DIVULGA_IMAGEM))
	       .append(" and NMIMAGEM IS NOT NULL and NMIMAGEM <> ''")
	       .append(" and DTINICIAL <= ").append(Sql.getValue(dataAtual))
	       .append(" and DTFINAL >= ").append(Sql.getValue(dataAtual));
    	addOrderBy(sql, null);
    	return findAll(null, sql.toString());
    }
    
    public void updateAllFlTipoAlteracaoInserido() throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("UPDATE ").append(tableName)
    	.append(" SET FLTIPOALTERACAO = '").append(BaseDomain.FLTIPOALTERACAO_INSERIDO).append("'");
    	updateAll(sql.toString());
    }
	
	public void updateResetReceberFotosNovamente() throws SQLException {
		updateResetFlTipoAlteracao(BaseDomain.FLTIPOALTERACAO_ORIGINAL, BaseDomain.FLTIPOALTERACAO_ALTERADO);
	}
	
	public void updateReceberFotosAoFalharRecebimentoCargaInicial() throws SQLException {
		updateResetFlTipoAlteracao(BaseDomain.FLTIPOALTERACAO_ORIGINAL, BaseDomain.FLTIPOALTERACAO_INSERIDO);
	}

	private void updateResetFlTipoAlteracao(String flTipoAlteracao, String flTipoAlteracaoFilter) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName)
		.append(" SET FLTIPOALTERACAO = '").append(flTipoAlteracao)
		.append("' WHERE FLTIPOALTERACAO = '").append(flTipoAlteracaoFilter).append("'");
		updateAll(sql.toString());
	}
	
	@Override
	public int countAllNaoAlterados() throws SQLException {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(*) as qtde FROM (");
		sql.append("select nmimagem from ").append(tableName).append(" ");
		addWhereAllNaoAlterados(sql);
		sql.append(" group by nmimagem) s");
		return getInt(sql.toString());
	}
	
	@Override
	protected void addWhereAllNaoAlterados(StringBuffer sql) {
		super.addWhereAllNaoAlterados(sql);
		sql.append(" and fltipodivulgainfo = ").append(Sql.getValue(DivulgaInfo.TIPO_DIVULGA_IMAGEM));
	}

}
