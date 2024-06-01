package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Sac;
import totalcross.sql.ResultSet;

public class SacDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Sac();
	}

    private static SacDao instance;
	
    public SacDao() {
        super(Sac.TABLE_NAME);
    }
    
    public static SacDao getInstance() {
        if (instance == null) {
            instance = new SacDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Sac sac = new Sac();
        sac.rowKey = rs.getString("rowkey");
        sac.cdEmpresa = rs.getString("cdEmpresa");
        sac.cdSac = rs.getString("cdSac");
        sac.cdTipoSac = rs.getString("cdTipoSac");
        sac.cdSubTipoSac = rs.getString("cdSubTipoSac");
        sac.dsSac = rs.getString("dsSac");
        sac.cdRepresentante = rs.getString("cdRepresentante");
        sac.cdCliente = rs.getString("cdCliente");
        sac.cdContato = rs.getString("cdContato");
        sac.cdUsuarioSac = rs.getString("cdUsuarioSac");
        sac.cdUsuario = rs.getString("cdUsuario");
        sac.cdStatusSac = rs.getString("cdStatusSac");
        sac.nuNotaFiscal = rs.getString("nuNotaFiscal");
        sac.nuPedido = rs.getString("nuPedido");
        sac.cdSerie = rs.getString("cdSerie");
        sac.dtCadastro = rs.getDate("dtCadastro");
        sac.dtPrevisao = rs.getDate("dtPrevisao");
        sac.dtConclusao = rs.getDate("dtConclusao");
        sac.dtAlteracao = rs.getDate("dtAlteracao");
        sac.flTipoAlteracao = rs.getString("flTipoAlteracao");
        sac.nuCarimbo = rs.getInt("nuCarimbo");
        sac.hrCadastro = rs.getString("hrCadastro");
        sac.hrConclusao = rs.getString("hrConclusao");
        sac.hrAlteracao = rs.getString("hrAlteracao");
        sac.cdAtendimento = rs.getInt("cdAtendimento");
        sac.flStatusAlterado = rs.getString("flStatusAlterado");
        sac.flSacExibido = rs.getString("flSacExibido");
        sac.flOrigem = rs.getString("flOrigem");
        return sac;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDSAC,");
        sql.append(" CDTIPOSAC,");
        sql.append(" CDSUBTIPOSAC,");
        sql.append(" DSSAC,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDCONTATO,");
        sql.append(" CDSTATUSSAC,");
        sql.append(" NUNOTAFISCAL,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDSERIE,");
        sql.append(" CDUSUARIOSAC,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTCADASTRO,");
        sql.append(" DTPREVISAO,");
        sql.append(" DTCONCLUSAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" HRCADASTRO,");
        sql.append(" HRCONCLUSAO,");
        sql.append(" FLSACEXIBIDO,");
        sql.append(" flStatusAlterado,");
        sql.append(" flOrigem,");
        sql.append(" CDATENDIMENTO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDSAC,");
        sql.append(" CDTIPOSAC,");
        sql.append(" CDSUBTIPOSAC,");
        sql.append(" DSSAC,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDCONTATO,");
        sql.append(" CDSTATUSSAC,");
        sql.append(" NUNOTAFISCAL,");
        sql.append(" NUPEDIDO,");
        sql.append(" CDSERIE,");
        sql.append(" CDUSUARIOSAC,");
        sql.append(" CDUSUARIO,");
        sql.append(" DTCADASTRO,");
        sql.append(" DTPREVISAO,");
        sql.append(" DTCONCLUSAO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NUCARIMBO,");
        sql.append(" HRCADASTRO,");
        sql.append(" HRCONCLUSAO,");
        sql.append(" FLORIGEM,");
        sql.append(" CDATENDIMENTO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO");
     }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Sac sac = (Sac) domain;
        sql.append(Sql.getValue(sac.cdEmpresa)).append(",");
        sql.append(Sql.getValue(sac.cdSac)).append(",");
        sql.append(Sql.getValue(sac.cdTipoSac)).append(",");
        sql.append(Sql.getValue(sac.cdSubTipoSac)).append(",");
        sql.append(Sql.getValue(sac.dsSac)).append(",");
        sql.append(Sql.getValue(sac.cdRepresentante)).append(",");
        sql.append(Sql.getValue(sac.cdCliente)).append(",");
        sql.append(Sql.getValue(sac.cdContato)).append(",");
        sql.append(Sql.getValue(sac.cdStatusSac)).append(",");
        sql.append(Sql.getValue(sac.nuNotaFiscal)).append(",");
        sql.append(Sql.getValue(sac.nuPedido)).append(",");
        sql.append(Sql.getValue(sac.cdSerie)).append(",");
        sql.append(Sql.getValue(sac.cdUsuarioSac)).append(",");
        sql.append(Sql.getValue(sac.cdUsuario)).append(",");
        sql.append(Sql.getValue(sac.dtCadastro)).append(",");
        sql.append(Sql.getValue(sac.dtPrevisao)).append(",");
        sql.append(Sql.getValue(sac.dtConclusao)).append(",");
        sql.append(Sql.getValue(sac.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(sac.nuCarimbo)).append(",");
        sql.append(Sql.getValue(sac.hrCadastro)).append(",");
        sql.append(Sql.getValue(sac.hrConclusao)).append(",");
        sql.append(Sql.getValue(sac.flOrigem)).append(",");
        sql.append(Sql.getValue(sac.cdAtendimento)).append(",");;
        sql.append(Sql.getValue(sac.dtAlteracao)).append(",");
        sql.append(Sql.getValue(sac.hrAlteracao));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Sac sac = (Sac) domain;
        sql.append(" CDTIPOSAC = ").append(Sql.getValue(sac.cdTipoSac)).append(",");
        sql.append(" CDSUBTIPOSAC = ").append(Sql.getValue(sac.cdSubTipoSac)).append(",");
        sql.append(" DSSAC = ").append(Sql.getValue(sac.dsSac)).append(",");
        sql.append(" CDREPRESENTANTE = ").append(Sql.getValue(sac.cdRepresentante)).append(",");
        sql.append(" CDCLIENTE = ").append(Sql.getValue(sac.cdCliente)).append(",");
        sql.append(" CDCONTATO = ").append(Sql.getValue(sac.cdContato)).append(",");
        sql.append(" CDSTATUSSAC = ").append(Sql.getValue(sac.cdStatusSac)).append(",");
        sql.append(" NUNOTAFISCAL = ").append(Sql.getValue(sac.nuNotaFiscal)).append(",");
        sql.append(" NUPEDIDO = ").append(Sql.getValue(sac.nuPedido)).append(",");
        sql.append(" CDSERIE = ").append(Sql.getValue(sac.cdSerie)).append(",");
        sql.append(" CDUSUARIOSAC = ").append(Sql.getValue(sac.cdUsuarioSac)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(sac.cdUsuario)).append(",");
        sql.append(" DTCADASTRO = ").append(Sql.getValue(sac.dtCadastro)).append(",");
        sql.append(" DTPREVISAO = ").append(Sql.getValue(sac.dtPrevisao)).append(",");
        sql.append(" DTCONCLUSAO = ").append(Sql.getValue(sac.dtConclusao)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(sac.flTipoAlteracao)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(sac.nuCarimbo)).append(",");
        sql.append(" HRCADASTRO = ").append(Sql.getValue(sac.hrCadastro)).append(",");
        sql.append(" HRCONCLUSAO = ").append(Sql.getValue(sac.hrConclusao)).append(",");
        sql.append(" FLSTATUSALTERADO = ").append(Sql.getValue(sac.flStatusAlterado)).append(",");
        sql.append(" FLSACEXIBIDO = ").append(Sql.getValue(sac.flSacExibido)).append(",");
        sql.append(" CDATENDIMENTO = ").append(Sql.getValue(sac.cdAtendimento)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(sac.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(sac.hrAlteracao));
        
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Sac sac = (Sac) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", sac.cdEmpresa);
		sqlWhereClause.addAndCondition("CDSAC = ", sac.cdSac);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", sac.cdCliente);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", sac.cdRepresentante);
		sqlWhereClause.addAndCondition("FLSTATUSALTERADO = ", sac.flStatusAlterado);
		if (sac.filterBySacNaoExibido) {
			sqlWhereClause.addAndOrConditionForced("FLSACEXIBIDO = ", null, "N");
		}
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    public void sacAtulizaStatus(BaseDomain domain) throws SQLException {
        Sac sac = (Sac) domain;
    	StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName);
		sql.append(" SET CDSTATUSSAC = '").append(sac.cdStatusSac);
		sql.append("' WHERE rowKey = '").append(sac.getRowKey()).append("' ");
		try {
			executeUpdate(sql.toString());
		} catch (ApplicationException e) {
			ExceptionUtil.handle(e);
		}
    }
    
}