package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.lavenderepda.business.domain.TipoSacAtividade;
import totalcross.sql.ResultSet;

public class TipoSacAtividadeDao extends LavendereCrudDbxDao {

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new TipoSacAtividade();
	}

	private static TipoSacAtividadeDao instance;

    public TipoSacAtividadeDao() {
        super(TipoSacAtividade.TABLE_NAME);
    }
    
    public static TipoSacAtividadeDao getInstance() {
        if (instance == null) {
            instance = new TipoSacAtividadeDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        TipoSacAtividade tipoSacAtividade = new TipoSacAtividade();
        tipoSacAtividade.rowKey = rs.getString("rowkey");
        tipoSacAtividade.cdEmpresa = rs.getString("cdEmpresa");
        tipoSacAtividade.cdTipoSac = rs.getString("cdTipoSac");
        tipoSacAtividade.cdAtividade = rs.getString("cdAtividade");
        tipoSacAtividade.cdUsuarioTipoSac = rs.getString("cdUsuarioTipoSac");
        tipoSacAtividade.dsTitulo = rs.getString("dsTitulo");
        tipoSacAtividade.nuSequencia = rs.getInt("nuSequencia");
        tipoSacAtividade.nuDiasUteis = rs.getInt("nuDiasUteis");
        tipoSacAtividade.nuDiasPrazoMax = rs.getInt("nuDiasPrazoMax");
        tipoSacAtividade.nuCarimbo = rs.getInt("nuCarimbo");
        tipoSacAtividade.flTipoAlteracao = rs.getString("flTipoAlteracao");
        tipoSacAtividade.cdUsuario = rs.getString("cdUsuario");
        return tipoSacAtividade;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDTIPOSAC,");
        sql.append(" CDATIVIDADE,");
        sql.append(" CDUSUARIOTIPOSAC,");
        sql.append(" DSTITULO,");
        sql.append(" NUSEQUENCIA,");
        sql.append(" NUDIASUTEIS,");
        sql.append(" NUDIASPRAZOMAX,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDTIPOSAC,");
        sql.append(" CDATIVIDADE,");
        sql.append(" CDUSUARIOTIPOSAC,");
        sql.append(" DSTITULO,");
        sql.append(" NUSEQUENCIA,");
        sql.append(" NUDIASUTEIS,");
        sql.append(" NUDIASPRAZOMAX,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoSacAtividade tipoSacAtividade = (TipoSacAtividade) domain;
        sql.append(Sql.getValue(tipoSacAtividade.cdEmpresa)).append(",");
        sql.append(Sql.getValue(tipoSacAtividade.cdTipoSac)).append(",");
        sql.append(Sql.getValue(tipoSacAtividade.cdAtividade)).append(",");
        sql.append(Sql.getValue(tipoSacAtividade.cdUsuarioTipoSac)).append(",");
        sql.append(Sql.getValue(tipoSacAtividade.dsTitulo)).append(",");
        sql.append(Sql.getValue(tipoSacAtividade.nuSequencia)).append(",");
        sql.append(Sql.getValue(tipoSacAtividade.nuDiasUteis)).append(",");
        sql.append(Sql.getValue(tipoSacAtividade.nuDiasPrazoMax)).append(",");
        sql.append(Sql.getValue(tipoSacAtividade.nuCarimbo)).append(",");
        sql.append(Sql.getValue(tipoSacAtividade.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(tipoSacAtividade.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoSacAtividade tipoSacAtividade = (TipoSacAtividade) domain;
        sql.append(" CDUSUARIOTIPOSAC = ").append(Sql.getValue(tipoSacAtividade.cdUsuarioTipoSac)).append(",");
        sql.append(" DSTITULO = ").append(Sql.getValue(tipoSacAtividade.dsTitulo)).append(",");
        sql.append(" NUSEQUENCIA = ").append(Sql.getValue(tipoSacAtividade.nuSequencia)).append(",");
        sql.append(" NUDIASUTEIS = ").append(Sql.getValue(tipoSacAtividade.nuDiasUteis)).append(",");
        sql.append(" NUDIASPRAZOMAX = ").append(Sql.getValue(tipoSacAtividade.nuDiasPrazoMax)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(tipoSacAtividade.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(tipoSacAtividade.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(tipoSacAtividade.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        TipoSacAtividade tipoSacAtividade = (TipoSacAtividade) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", tipoSacAtividade.cdEmpresa);
		sqlWhereClause.addAndCondition("CDTIPOSAC = ", tipoSacAtividade.cdTipoSac);
		sqlWhereClause.addAndCondition("CDATIVIDADE = ", tipoSacAtividade.cdAtividade);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
}