package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.VerbaGrupoSaldo;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;


public class VerbaGrupoSaldoDao extends LavendereCrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new VerbaGrupoSaldo();
	}

	private static VerbaGrupoSaldoDao instance;

    public VerbaGrupoSaldoDao() {
        super(VerbaGrupoSaldo.TABLE_NAME);
    }

    public static VerbaGrupoSaldoDao getInstance() {
        if (instance == null) {
            instance = new VerbaGrupoSaldoDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
    	VerbaGrupoSaldo verbaGrupoSaldo = new VerbaGrupoSaldo();
    	verbaGrupoSaldo.rowKey = rs.getString("rowkey");
    	verbaGrupoSaldo.cdEmpresa = rs.getString("cdEmpresa");
    	verbaGrupoSaldo.cdRepresentante = rs.getString("cdRepresentante");
    	verbaGrupoSaldo.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
    	verbaGrupoSaldo.flOrigemSaldo = rs.getString("flOrigemSaldo");
    	verbaGrupoSaldo.cdVerbaGrupo = rs.getInt("cdVerbaGrupo");
    	verbaGrupoSaldo.vlSaldoInicial = ValueUtil.round(rs.getDouble("vlSaldoInicial"));
    	verbaGrupoSaldo.vlSaldo = ValueUtil.round(rs.getDouble("vlSaldo"));
    	verbaGrupoSaldo.vlTolerancia = ValueUtil.round(rs.getDouble("vlTolerancia"));
    	verbaGrupoSaldo.vlPctToleranciaDesc = ValueUtil.round(rs.getDouble("vlPctToleranciaDesc"));
    	verbaGrupoSaldo.dtVigenciaInicial = rs.getDate("dtVigenciaInicial");
    	verbaGrupoSaldo.dtVigenciaFinal = rs.getDate("dtVigenciaFinal");
        verbaGrupoSaldo.nuCarimbo = rs.getInt("nuCarimbo");
        verbaGrupoSaldo.flTipoAlteracao = rs.getString("flTipoAlteracao");
        if (LavenderePdaConfig.isUsaMotivoPendencia()) {
	        verbaGrupoSaldo.cdMotivoPendencia = rs.getString("cdMotivoPendencia");
	        verbaGrupoSaldo.nuOrdemLiberacao = rs.getInt("nuOrdemLiberacao");
        }
        verbaGrupoSaldo.cdUsuario = rs.getString("cdUsuario");
        return verbaGrupoSaldo;
    }
    
    protected BaseDomain populateOnlyPk(ResultSet rs) throws java.sql.SQLException {
    	VerbaGrupoSaldo verbaGrupoSaldo = new VerbaGrupoSaldo();
    	verbaGrupoSaldo.rowKey = rs.getString("rowkey");
    	verbaGrupoSaldo.cdEmpresa = rs.getString("cdEmpresa");
    	verbaGrupoSaldo.cdRepresentante = rs.getString("cdRepresentante");
    	verbaGrupoSaldo.cdGrupoProduto1 = rs.getString("cdGrupoProduto1");
        return verbaGrupoSaldo;
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
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" FLORIGEMSALDO,");
        sql.append(" CDVERBAGRUPO,");
        sql.append(" VLSALDOINICIAL,");
        sql.append(" VLSALDO,");
        sql.append(" VLTOLERANCIA,");
        sql.append(" VLPCTTOLERANCIADESC,");
        sql.append(" DTVIGENCIAINICIAL,");
        sql.append(" DTVIGENCIAFINAL,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        if (LavenderePdaConfig.isUsaMotivoPendencia()) {
	        sql.append(" CDMOTIVOPENDENCIA,");
	        sql.append(" NUORDEMLIBERACAO,");
        }
        sql.append(" CDUSUARIO");
    }

	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDGRUPOPRODUTO1,");
        sql.append(" FLORIGEMSALDO,");
        sql.append(" CDVERBAGRUPO,");
        sql.append(" VLSALDOINICIAL,");
        sql.append(" VLSALDO,");
        sql.append("VLTOLERANCIA,");
        sql.append(" VLPCTTOLERANCIADESC,");
        sql.append(" DTVIGENCIAINICIAL,");
        sql.append(" DTVIGENCIAFINAL,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        VerbaGrupoSaldo verbaGrupoSaldo = (VerbaGrupoSaldo) domain;
        sql.append(Sql.getValue(verbaGrupoSaldo.cdEmpresa)).append(",");
        sql.append(Sql.getValue(verbaGrupoSaldo.cdRepresentante)).append(",");
        sql.append(Sql.getValue(verbaGrupoSaldo.cdGrupoProduto1)).append(",");
        sql.append(Sql.getValue(verbaGrupoSaldo.flOrigemSaldo)).append(",");
        sql.append(Sql.getValue(verbaGrupoSaldo.cdVerbaGrupo)).append(",");
        sql.append(Sql.getValue(verbaGrupoSaldo.vlSaldoInicial)).append(",");
        sql.append(Sql.getValue(verbaGrupoSaldo.vlSaldo)).append(",");
        sql.append(Sql.getValue(verbaGrupoSaldo.vlTolerancia)).append(",");
        sql.append(Sql.getValue(verbaGrupoSaldo.vlPctToleranciaDesc)).append(",");
        sql.append(Sql.getValue(verbaGrupoSaldo.dtVigenciaInicial)).append(",");
        sql.append(Sql.getValue(verbaGrupoSaldo.dtVigenciaFinal)).append(",");
        sql.append(Sql.getValue(verbaGrupoSaldo.nuCarimbo)).append(",");
        sql.append(Sql.getValue(verbaGrupoSaldo.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(verbaGrupoSaldo.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	VerbaGrupoSaldo verbaGrupoSaldo = (VerbaGrupoSaldo) domain;
        sql.append(" VLSALDOINICIAL = ").append(Sql.getValue(verbaGrupoSaldo.vlSaldoInicial)).append(",");
        sql.append(" VLSALDO = ").append(Sql.getValue(verbaGrupoSaldo.vlSaldo)).append(",");
        sql.append(" VLTOLERANCIA = ").append(Sql.getValue(verbaGrupoSaldo.vlTolerancia)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(verbaGrupoSaldo.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(verbaGrupoSaldo.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(verbaGrupoSaldo.cdUsuario)).append(",");
        sql.append(" VLPCTTOLERANCIADESC = ").append(Sql.getValue(verbaGrupoSaldo.vlPctToleranciaDesc)).append(",");
        sql.append(" DTVIGENCIAINICIAL = ").append(Sql.getValue(verbaGrupoSaldo.dtVigenciaInicial)).append(",");
        sql.append(" DTVIGENCIAFINAL = ").append(Sql.getValue(verbaGrupoSaldo.dtVigenciaFinal));
    }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
    	VerbaGrupoSaldo verbagruposaldo = (VerbaGrupoSaldo) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", verbagruposaldo.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", verbagruposaldo.cdRepresentante);
		sqlWhereClause.addAndCondition("CDGRUPOPRODUTO1 = ", verbagruposaldo.cdGrupoProduto1);
		sqlWhereClause.addAndConditionIn(" CDGRUPOPRODUTO1", verbagruposaldo.cdGrupoProduto1Array);
		sqlWhereClause.addAndCondition("FLORIGEMSALDO = ", verbagruposaldo.flOrigemSaldo);
		sqlWhereClause.addAndCondition("CDVERBAGRUPO = ", verbagruposaldo.cdVerbaGrupo);
		//--
		sql.append(sqlWhereClause.getSql());
		if (ValueUtil.isNotEmpty(verbagruposaldo.dtVigenciaInicial)) {
			sql.append(" and (DTVIGENCIAINICIAL <= ").append(Sql.getValue(verbagruposaldo.dtVigenciaInicial)).append(" or DTVIGENCIAINICIAL = ").append(Sql.getValue(DateUtil.DATE_FOR_NULL_VALUE_SQL)).append(")");
		}
		if (ValueUtil.isNotEmpty(verbagruposaldo.dtVigenciaFinal)) {
			sql.append(" and (DTVIGENCIAFINAL >= ").append(Sql.getValue(verbagruposaldo.dtVigenciaFinal)).append(" or DTVIGENCIAFINAL = ").append(Sql.getValue(DateUtil.DATE_FOR_NULL_VALUE_SQL)).append(")");
		}
    }

	public HashMap<String, VerbaGrupoSaldo> mesclaHashComGruposNaoExistentes(VerbaGrupoSaldo verGrupoSaldoFilter, HashMap<String, VerbaGrupoSaldo> hashGrupo) throws SQLException {
		if (hashGrupo == null ) hashGrupo = new HashMap<String, VerbaGrupoSaldo>();
    	StringBuffer sql = getSqlBuffer();
    	sql.append("select distinct cdEmpresa, cdRepresentante, cdGrupoProduto1, rowKey from ").append(VerbaGrupoSaldo.TABLE_NAME);
    	addWhereByExample(verGrupoSaldoFilter, sql);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		VerbaGrupoSaldo verbaGrupoSaldo;
			while (rs.next()) {
				verbaGrupoSaldo = (VerbaGrupoSaldo) populateOnlyPk(rs);
				if (hashGrupo.get(verbaGrupoSaldo.cdGrupoProduto1) == null)	hashGrupo.put(verbaGrupoSaldo.cdGrupoProduto1, verbaGrupoSaldo);
			}
			return hashGrupo;
		}
	}

}