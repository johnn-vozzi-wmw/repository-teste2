package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.EstoqueRep;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class EstoqueRepDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new EstoqueRep();
	}

    private static EstoqueRepDbxDao instance;

    public EstoqueRepDbxDao() {
        super(EstoqueRep.TABLE_NAME);
    }
    
    public static EstoqueRepDbxDao getInstance() {
        if (instance == null) {
            instance = new EstoqueRepDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        EstoqueRep estoqueRep = new EstoqueRep();
        estoqueRep.rowKey = rs.getString("rowkey");
        estoqueRep.cdEmpresa = rs.getString("cdEmpresa");
        estoqueRep.cdRepresentante = rs.getString("cdRepresentante");
        estoqueRep.cdProduto = rs.getString("cdProduto");
        estoqueRep.cdItemGrade1 = rs.getString("cdItemGrade1");
        estoqueRep.cdItemGrade2 = rs.getString("cdItemGrade2");
        estoqueRep.cdItemGrade3 = rs.getString("cdItemGrade3");
        estoqueRep.cdLocalEstoque = rs.getString("cdLocalEstoque");
        estoqueRep.qtEstoque = ValueUtil.round(rs.getDouble("qtEstoque"));
        estoqueRep.dtEstoque = rs.getDate("dtEstoque");
        estoqueRep.hrEstoque = rs.getString("hrEstoque");
        estoqueRep.flTipoAlteracao = rs.getString("flTipoAlteracao");
        estoqueRep.nuCarimbo = rs.getInt("nuCarimbo");
        estoqueRep.cdUsuario = rs.getString("cdUsuario");
        return estoqueRep;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDITEMGRADE1,");
        sql.append(" CDITEMGRADE2,");
        sql.append(" CDITEMGRADE3,");
        sql.append(" CDLOCALESTOQUE,");
        sql.append(" QTESTOQUE,");
        sql.append(" DTESTOQUE,");
        sql.append(" HRESTOQUE,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO" );
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPRODUTO,");
        sql.append(" CDITEMGRADE1,");
        sql.append(" CDITEMGRADE2,");
        sql.append(" CDITEMGRADE3,");
        sql.append(" CDLOCALESTOQUE,");
        sql.append(" QTESTOQUE,");
        sql.append(" DTESTOQUE,");
        sql.append(" HRESTOQUE,");
        sql.append(" FLTIPODEVOLUCAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        EstoqueRep estoqueRep = (EstoqueRep) domain;
        sql.append(Sql.getValue(estoqueRep.cdEmpresa)).append(",");
        sql.append(Sql.getValue(estoqueRep.cdRepresentante)).append(",");
        sql.append(Sql.getValue(estoqueRep.cdProduto)).append(",");
        sql.append(Sql.getValue(estoqueRep.cdItemGrade1)).append(",");
        sql.append(Sql.getValue(estoqueRep.cdItemGrade2)).append(",");
        sql.append(Sql.getValue(estoqueRep.cdItemGrade3)).append(",");
        sql.append(Sql.getValue(estoqueRep.cdLocalEstoque)).append(",");
        sql.append(Sql.getValue(estoqueRep.qtEstoque)).append(",");
        sql.append(Sql.getValue(estoqueRep.dtEstoque)).append(",");
        sql.append(Sql.getValue(estoqueRep.hrEstoque)).append(",");
        sql.append(Sql.getValue(estoqueRep.flTipoDevolucao)).append(",");
        sql.append(Sql.getValue(estoqueRep.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(estoqueRep.nuCarimbo)).append(",");
        sql.append(Sql.getValue(estoqueRep.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        EstoqueRep estoqueRep = (EstoqueRep) domain;
        sql.append(" QTESTOQUE = ").append(Sql.getValue(estoqueRep.qtEstoque)).append(",");
        sql.append(" DTESTOQUE = ").append(Sql.getValue(estoqueRep.dtEstoque)).append(",");
        sql.append(" HRESTOQUE = ").append(Sql.getValue(estoqueRep.hrEstoque)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(estoqueRep.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(estoqueRep.nuCarimbo)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(estoqueRep.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        EstoqueRep estoqueRep = (EstoqueRep) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", estoqueRep.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", estoqueRep.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPRODUTO = ", estoqueRep.cdProduto);
		sqlWhereClause.addAndCondition("CDITEMGRADE1 = ", estoqueRep.cdItemGrade1);
		sqlWhereClause.addAndCondition("CDITEMGRADE2 = ", estoqueRep.cdItemGrade2);
		sqlWhereClause.addAndCondition("CDITEMGRADE3 = ", estoqueRep.cdItemGrade3);
		sqlWhereClause.addAndCondition("CDLOCALESTOQUE = ", estoqueRep.cdLocalEstoque);
		sqlWhereClause.addAndCondition("DTESTOQUE <= ", estoqueRep.dtEstoqueFilter);
		if (estoqueRep.deleteRegistroEnviadoServidor) {
			sqlWhereClause.addAndConditionForced("FLTIPOALTERACAO = ", estoqueRep.flTipoAlteracao);
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }
 
	@Override
	protected boolean isNotSaveTabelasEnvio() {
		return true;
	}
	
	public List<EstoqueRep> findDevolucaoEstoque(String cdEmpresa, String cdRepresentante) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" SELECT er.cdempresa, er.cdrepresentante, er.cdproduto, pr.dsproduto, dtestoque, hrestoque, SUM(er.qtestoque) as estoque , (select vlunitario");
		sql.append(" 		FROM tblvpitemtabelapreco itp ");
		sql.append(" 		INNER JOIN tblvptabelapreco tp ON (tp.cdempresa = itp.cdempresa AND tp.cdrepresentante = itp.cdrepresentante AND tp.cdtabelapreco = itp.cdtabelapreco ");
		sql.append("			AND tp.cdusuario = itp.cdusuario AND flDevolucaoEstoque = 'S')"); 
		sql.append(" 		WHERE itp.cdempresa = er.cdempresa AND itp.cdrepresentante = er.cdrepresentante AND itp.cdproduto = er.cdproduto");
		sql.append(" 		LIMIT 1) as vlunitario ");
		sql.append(" FROM ");
		sql.append(tableName).append(" er ");
		sql.append(" inner join tblvpproduto pr on(pr.cdempresa = er.cdempresa and pr.cdrepresentante = er.cdrepresentante and pr.cdproduto = er.cdproduto and pr.cdusuario = er.cdusuario)");
		sql.append(" WHERE pr.cdempresa = '").append(cdEmpresa).append("'");
		sql.append(" AND pr.cdrepresentante = '").append(cdRepresentante).append("'");
		sql.append(" and dtestoque = (select max(dtestoque) from tblvpestoquerep limit 1) ");
		sql.append(" group by er.cdempresa, er.cdrepresentante, er.cdproduto, pr.dsproduto, dtestoque, hrestoque ");
		sql.append(" order by pr.dsproduto");
    	List<EstoqueRep> list = new ArrayList<>();
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		while (rs.next()) {
    			EstoqueRep estoqueRep = new EstoqueRep();
    			estoqueRep.cdEmpresa = rs.getString("cdempresa");
    			estoqueRep.cdRepresentante = rs.getString("cdrepresentante");
    			estoqueRep.cdProduto = rs.getString("cdproduto");
    			estoqueRep.dsProduto = rs.getString("dsproduto");
    			estoqueRep.qtEstoque = rs.getDouble("estoque");
    			estoqueRep.dtEstoque = rs.getDate("dtestoque");
    			estoqueRep.hrEstoque = rs.getString("hrestoque");
    			estoqueRep.vlUnitario = rs.getDouble("vlUnitario");
    			estoqueRep.vlTotal = estoqueRep.vlUnitario * estoqueRep.qtEstoque;
    			list.add(estoqueRep);
    		}
		}
    	return list;
    }
}