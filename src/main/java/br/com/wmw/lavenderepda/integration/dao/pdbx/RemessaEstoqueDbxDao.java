package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoRemessaComboBox.TipoRemessaEnum;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;

public class RemessaEstoqueDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RemessaEstoque();
	}

    private static RemessaEstoqueDbxDao instance;

    public RemessaEstoqueDbxDao() {
        super(RemessaEstoque.TABLE_NAME); 
    }
    
    public static RemessaEstoqueDbxDao getInstance() {
        if (instance == null) {
            instance = new RemessaEstoqueDbxDao();
        }
        return instance;
    }
    
    @Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        RemessaEstoque remessaEstoque = new RemessaEstoque();
        remessaEstoque.rowKey = rs.getString("rowkey");
        remessaEstoque.cdEmpresa = rs.getString("cdEmpresa");
        remessaEstoque.cdRepresentante = rs.getString("cdRepresentante");
        remessaEstoque.nuNotaRemessa = rs.getString("nuNotaRemessa");
        remessaEstoque.nuSerieRemessa = rs.getString("nuSerieRemessa");
        remessaEstoque.cdLocalEstoque = rs.getString("cdLocalEstoque");
        remessaEstoque.dtRemessa = rs.getDate("dtRemessa");
        remessaEstoque.hrRemessa = rs.getString("hrRemessa");
        remessaEstoque.vlChaveAcesso = rs.getString("vlChaveAcesso");
        remessaEstoque.flEstoqueLiberado = rs.getString("flEstoqueLiberado");
        remessaEstoque.flFinalizada = rs.getString("flFinalizada");
        if (LavenderePdaConfig.usaGeracaoRemessaEstoqueNoEquipamento) {
        	remessaEstoque.flTipoRemessa = rs.getString("flTipoRemessa");
        }
        remessaEstoque.cdUsuario = rs.getString("cdUsuario");
        remessaEstoque.nuCarimbo = rs.getInt("nuCarimbo");
        remessaEstoque.flTipoAlteracao = rs.getString("flTipoAlteracao");
        remessaEstoque.dtAlteracao = rs.getDate("dtAlteracao");
        remessaEstoque.hrAlteracao = rs.getString("hrAlteracao");
        remessaEstoque.dtFinalizacao = rs.getDate("dtFinalizacao");
        remessaEstoque.hrFinalizacao = rs.getString("hrFinalizacao");
        return remessaEstoque;
    }
    
	@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    @Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NUNOTAREMESSA,");
        sql.append(" NUSERIEREMESSA,");
        sql.append(" CDLOCALESTOQUE,");
        sql.append(" DTREMESSA,");
        sql.append(" HRREMESSA,");
        sql.append(" VLCHAVEACESSO,");
        sql.append(" FLESTOQUELIBERADO,");
        sql.append(" FLFINALIZADA,");
        if (LavenderePdaConfig.usaGeracaoRemessaEstoqueNoEquipamento) {
        	sql.append(" FLTIPOREMESSA,");
        }
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" DTFINALIZACAO,");
        sql.append(" HRFINALIZACAO");
    }
    
	@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    @Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" NUNOTAREMESSA,");
        sql.append(" NUSERIEREMESSA,");
        sql.append(" CDLOCALESTOQUE,");
        sql.append(" DTREMESSA,");
        sql.append(" HRREMESSA,");
        sql.append(" VLCHAVEACESSO,");
        sql.append(" FLESTOQUELIBERADO,");
        if (LavenderePdaConfig.usaGeracaoRemessaEstoqueNoEquipamento) {
        	sql.append(" FLTIPOREMESSA,");
        }
        sql.append(" CDUSUARIO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" DTFINALIZACAO,");
        sql.append(" HRFINALIZACAO");
    }

    @Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        RemessaEstoque remessaEstoque = (RemessaEstoque) domain;
        sql.append(Sql.getValue(remessaEstoque.cdEmpresa)).append(",");
        sql.append(Sql.getValue(remessaEstoque.cdRepresentante)).append(",");
        sql.append(Sql.getValue(remessaEstoque.nuNotaRemessa)).append(",");
        sql.append(Sql.getValue(remessaEstoque.nuSerieRemessa)).append(",");
        sql.append(Sql.getValue(remessaEstoque.cdLocalEstoque)).append(",");
        sql.append(Sql.getValue(remessaEstoque.dtRemessa)).append(",");
        sql.append(Sql.getValue(remessaEstoque.hrRemessa)).append(",");
        sql.append(Sql.getValue(remessaEstoque.vlChaveAcesso)).append(",");
        sql.append(Sql.getValue(remessaEstoque.flEstoqueLiberado)).append(",");
        if (LavenderePdaConfig.usaGeracaoRemessaEstoqueNoEquipamento) {
        	sql.append(Sql.getValue(remessaEstoque.flTipoRemessa)).append(",");
        }
        sql.append(Sql.getValue(remessaEstoque.cdUsuario)).append(",");
        sql.append(Sql.getValue(remessaEstoque.nuCarimbo)).append(",");
        sql.append(Sql.getValue(remessaEstoque.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(remessaEstoque.dtAlteracao)).append(",");
        sql.append(Sql.getValue(remessaEstoque.hrAlteracao)).append(",");
        sql.append(Sql.getValue(remessaEstoque.dtFinalizacao)).append(",");
        sql.append(Sql.getValue(remessaEstoque.hrFinalizacao));
    }

    @Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        RemessaEstoque remessaEstoque = (RemessaEstoque) domain;
        sql.append(" CDLOCALESTOQUE = ").append(Sql.getValue(remessaEstoque.cdLocalEstoque)).append(",");
        sql.append(" DTREMESSA = ").append(Sql.getValue(remessaEstoque.dtRemessa)).append(",");
        sql.append(" HRREMESSA = ").append(Sql.getValue(remessaEstoque.hrRemessa)).append(",");
        sql.append(" VLCHAVEACESSO = ").append(Sql.getValue(remessaEstoque.vlChaveAcesso)).append(",");
        sql.append(" FLESTOQUELIBERADO = ").append(Sql.getValue(remessaEstoque.flEstoqueLiberado)).append(",");
        sql.append(" FLFINALIZADA = ").append(Sql.getValue(remessaEstoque.flFinalizada)).append(",");
        if (LavenderePdaConfig.usaGeracaoRemessaEstoqueNoEquipamento) {
        	sql.append(" FLTIPOREMESSA = ").append(Sql.getValue(remessaEstoque.flTipoRemessa)).append(",");
        }
        sql.append(" CDUSUARIO = ").append(Sql.getValue(remessaEstoque.cdUsuario)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(remessaEstoque.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(remessaEstoque.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(remessaEstoque.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(remessaEstoque.hrAlteracao)).append(",");
        sql.append(" DTFINALIZACAO = ").append(Sql.getValue(remessaEstoque.dtFinalizacao)).append(",");
        sql.append(" HRFINALIZACAO = ").append(Sql.getValue(remessaEstoque.hrFinalizacao));
    }
    
    @Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        RemessaEstoque remessaEstoque = (RemessaEstoque) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", remessaEstoque.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", remessaEstoque.cdRepresentante);
		adicionaFiltroEstoqueLiberado(remessaEstoque, sqlWhereClause);
		adicionaFiltroRemessaFinalizada(remessaEstoque, sqlWhereClause);
		sqlWhereClause.addAndCondition("DTREMESSA <= ", remessaEstoque.dtRemessa);
		if (TipoRemessaEnum.TIPOREMESSA_EMPRESA.cdTipo.equals(remessaEstoque.flTipoRemessa)) {
			sqlWhereClause.addAndCondition("(FLTIPOREMESSA <> 'R' OR FLTIPOREMESSA IS NULL)");
		} else if (TipoRemessaEnum.TIPOREMESSA_REPRESENTANTE.cdTipo.equals(remessaEstoque.flTipoRemessa)) {
			sqlWhereClause.addAndCondition("FLTIPOREMESSA = ", remessaEstoque.flTipoRemessa);
		}
		sqlWhereClause.addAndCondition("DTREMESSA <= ", remessaEstoque.dtRemessa);
		sql.append(sqlWhereClause.getSql());
    }
    
    private void adicionaFiltroEstoqueLiberado(RemessaEstoque remessaEstoque, SqlWhereClause sqlWhereClause) {
		if (ValueUtil.valueEquals(ValueUtil.VALOR_SIM, remessaEstoque.flEstoqueLiberado)) {
			sqlWhereClause.addAndCondition("FLESTOQUELIBERADO = ", remessaEstoque.flEstoqueLiberado);
		} else if (ValueUtil.valueEquals(ValueUtil.VALOR_NAO, remessaEstoque.flEstoqueLiberado)) {
			sqlWhereClause.addStartAndMultipleCondition();
			sqlWhereClause.addAndConditionNotEquals("FLESTOQUELIBERADO ", "S");
			sqlWhereClause.addOrCondition("FLESTOQUELIBERADO is null");
			sqlWhereClause.addEndMultipleCondition();
		}	
	}
	
	private void adicionaFiltroRemessaFinalizada(RemessaEstoque remessaEstoque, SqlWhereClause sqlWhereClause) {
		if (ValueUtil.valueEquals(RemessaEstoque.FL_FINALIZADA, remessaEstoque.flFinalizada)) {
			sqlWhereClause.addAndCondition("FLFINALIZADA = ", ValueUtil.VALOR_SIM);
		} else if (ValueUtil.valueEquals(ValueUtil.VALOR_NAO, remessaEstoque.flFinalizada)) {
			sqlWhereClause.addStartAndMultipleCondition();
			sqlWhereClause.addAndConditionNotEquals("FLFINALIZADA ", "S");
			sqlWhereClause.addOrCondition("FLFINALIZADA is null");
			sqlWhereClause.addEndMultipleCondition();
		}
	}
 
	public List<RemessaEstoque> findRemessaEstoqueDevolucao(String cdEmpresa, String cdRepresentante) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    			
		sql.append("select  nuNotaRemessa, nuSerieRemessa, re.cdLocalEstoque  from tblvpremessaestoque re ");
		sql.append("INNER JOIN tblvpestoquerep es on (es.cdempresa = re.cdempresa and es.cdrepresentante = re.cdrepresentante and es.cdlocalestoque = re.cdlocalestoque) ");
		sql.append(" WHERE re.cdempresa = '").append(cdEmpresa).append("'");
		sql.append(" AND re.cdrepresentante = '").append(cdRepresentante).append("'");
		sql.append("and dtestoque = (select max(dtestoque) from tblvpestoquerep limit 1) ");
		sql.append("GROUP BY nuNotaRemessa, nuSerieRemessa, re.cdLocalEstoque ");
    	List<RemessaEstoque> list = new ArrayList<>();
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		while (rs.next()) {
    			RemessaEstoque remesa = new RemessaEstoque();
    			remesa.nuNotaRemessa = rs.getString("nuNotaRemessa");
    			remesa.nuSerieRemessa = rs.getString("nuSerieRemessa");
    			remesa.cdLocalEstoque = rs.getString("cdLocalEstoque");
    			list.add(remesa);
    		}
		}
    	return list;
    }
	public List<RemessaEstoque> buscaRemessaLiberadasPor(String cdEmpresa, String cdRepresentante) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT * ");
		sql.append("FROM TBLVPREMESSAESTOQUE tb ");
		sql.append("WHERE tb.CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
		sql.append("  AND tb.CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
		sql.append("  AND tb.FLESTOQUELIBERADO = 'S' ");
		sql.append("  AND (tb.FLFINALIZADA = 'N' OR tb.FLFINALIZADA = '' OR tb.FLFINALIZADA IS NULL) ");
		sql.append("order by tb.dtRemessa ");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			List<RemessaEstoque> list = new ArrayList<>();
			RemessaEstoque remessa;
			while (rs.next()) {
				remessa = new RemessaEstoque();
				remessa.nuNotaRemessa = rs.getString("nuNotaRemessa");
				remessa.nuSerieRemessa = rs.getString("nuSerieRemessa");
				remessa.cdLocalEstoque = rs.getString("cdLocalEstoque");
				remessa.dtRemessa = rs.getDate("dtRemessa");
				list.add(remessa);
			}
			return list;
		}
	}
	
	public List<Object[]> buscaProdutosRemessaPor(String cdEmpresa, String cdRepresentante) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT pr.cdproduto, pr.dsproduto, ");
		sql.append("SUM(Case WHEN es.florigemestoque = 'E' THEN es.qtestoqueremessa ELSE 0 END) AS remessa, ");
		sql.append("SUM(Case WHEN es.florigemestoque = 'E' THEN re.qtEstoque ELSE 0 END) AS retorno, ");
		sql.append(" SUM(CASE WHEN flOrigemEstoque = 'E' THEN es.qtestoque ELSE - es.qtestoque END ) as saldo ");
		sql.append("FROM TBLVPREMESSAESTOQUE tb ");
		sql.append("INNER JOIN tblvpestoque es on (es.cdempresa = tb.cdempresa and es.cdrepresentante = tb.cdrepresentante and es.cdlocalestoque = tb.cdlocalestoque) ");
		sql.append("LEFT JOIN tblvpestoquerep re on (re.cdempresa = es.cdempresa and re.cdrepresentante = es.cdrepresentante and re.cdproduto = es.cdproduto and re.cdlocalestoque = es.cdlocalestoque) "); 
		sql.append("INNER JOIN tblvpproduto pr on (pr.cdempresa = es.cdempresa and pr.cdrepresentante = es.cdrepresentante and pr.cdproduto = es.cdproduto) ");
		sql.append("WHERE tb.CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
		sql.append("  AND tb.CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
		sql.append("  AND tb.FLESTOQUELIBERADO = 'S' ");
		sql.append("  AND (tb.FLFINALIZADA = 'N' OR tb.FLFINALIZADA = '' OR tb.FLFINALIZADA IS NULL) ");
		sql.append("group by pr.cdproduto, pr.dsproduto ");
		sql.append("order by pr.dsproduto ");
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
    		List<Object[]> list = new ArrayList<>();
			while (rs.next()) {
				Object[] array = new Object[6];
				array[0] = rs.getString("dsproduto");
				array[1] = rs.getString("cdproduto");
				array[2] = rs.getDouble("remessa");
				array[3] = rs.getDouble("remessa") - rs.getDouble("saldo") - rs.getDouble("retorno");
				array[4] = rs.getDouble("retorno");
				array[5] = rs.getDouble("saldo");
				list.add(array);
			}
			return list;
		}
	}
}