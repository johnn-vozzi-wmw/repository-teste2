package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Recado;
import totalcross.sql.ResultSet;

public class RecadoPdbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new Recado();
	}

    private static RecadoPdbxDao instance;

    public RecadoPdbxDao() {
        super(Recado.TABLE_NAME);
    }

    public static RecadoPdbxDao getInstance() {
        if (instance == null) {
            instance = new RecadoPdbxDao();
        }
        return instance;
    }

    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        Recado recado = new Recado();
        recado.rowKey = rs.getString("rowkey");
        recado.cdRecado = rs.getString("cdRecado");
        recado.cdUsuarioRemetente = rs.getString("cdUsuarioremetente");
        recado.cdUsuarioDestinatario = rs.getString("cdUsuariodestinatario");
        recado.cdEmpresaCliente = rs.getString("cdEmpresaCliente");
        recado.cdRepresentanteCliente = rs.getString("cdRepresentanteCliente");
        recado.cdCliente = rs.getString("cdCliente");
        recado.dsAssunto = rs.getString("dsAssunto");
        recado.dsRecado = rs.getString("dsRecado");
        recado.dtEnvio = rs.getDate("dtEnvio");
        recado.hrEnvio = rs.getString("hrEnvio");
        recado.dtCadastro = rs.getDate("dtCadastro");
        recado.hrCadastro = rs.getString("hrCadastro");
        recado.dtLeitura = rs.getDate("dtLeitura");
        recado.hrLeitura = rs.getString("hrLeitura");
        recado.flLido = rs.getString("flLido");
        recado.cdStatusEnvio = rs.getString("cdStatusEnvio");
        recado.flObrigaResposta = rs.getString("flObrigaResposta");
        recado.flRespostaEnviada = rs.getString("flRespostaEnviada");
        recado.flTipoRecado = rs.getString("flTipoRecado");
        recado.cdRecadoRelacionado = rs.getString("cdRecadoRelacionado");
        recado.flTipoAlteracao = rs.getString("flTipoAlteracao");
        recado.cdUsuario = rs.getString("cdUsuario");

        return recado;
    }

    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDRECADO,");
        sql.append(" CDUSUARIOREMETENTE,");
        sql.append(" CDUSUARIODESTINATARIO,");
        sql.append(" CDEMPRESACLIENTE,");
        sql.append(" CDREPRESENTANTECLIENTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" DSASSUNTO,");
        sql.append(" DSRECADO,");
        sql.append(" DTENVIO,");
        sql.append(" HRENVIO,");
        sql.append(" DTCADASTRO,");
        sql.append(" HRCADASTRO,");
        sql.append(" DTLEITURA,");
        sql.append(" HRLEITURA,");
        sql.append(" FLLIDO,");
        sql.append(" CDSTATUSENVIO,");
        sql.append(" FLOBRIGARESPOSTA,");
        sql.append(" FLRESPOSTAENVIADA,");
        sql.append(" FLTIPORECADO,");
        sql.append(" CDRECADORELACIONADO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException {
        sql.append(" CDRECADO,");
        sql.append(" CDUSUARIOREMETENTE,");
        sql.append(" CDUSUARIODESTINATARIO,");
        sql.append(" CDEMPRESACLIENTE,");
        sql.append(" CDREPRESENTANTECLIENTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" DSASSUNTO,");
        sql.append(" DSRECADO,");
        sql.append(" DTENVIO,");
        sql.append(" HRENVIO,");
        sql.append(" DTCADASTRO,");
        sql.append(" HRCADASTRO,");
        sql.append(" DTLEITURA,");
        sql.append(" HRLEITURA,");
        sql.append(" FLLIDO,");
        sql.append(" CDSTATUSENVIO,");
        sql.append(" FLOBRIGARESPOSTA,");
        sql.append(" FLRESPOSTAENVIADA,");
        sql.append(" FLTIPORECADO,");
        sql.append(" CDRECADORELACIONADO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Recado recado = (Recado) domain;
        sql.append(Sql.getValue(recado.cdRecado)).append(",");
        sql.append(Sql.getValue(recado.cdUsuarioRemetente)).append(",");
        sql.append(Sql.getValue(recado.cdUsuarioDestinatario)).append(",");
        sql.append(Sql.getValue(recado.cdEmpresaCliente)).append(",");
        sql.append(Sql.getValue(recado.cdRepresentanteCliente)).append(",");
        sql.append(Sql.getValue(recado.cdCliente)).append(",");
        sql.append(Sql.getValue(recado.dsAssunto)).append(",");
        sql.append(Sql.getValue(recado.dsRecado)).append(",");
        sql.append(Sql.getValue(recado.dtEnvio)).append(",");
        sql.append(Sql.getValue(recado.hrEnvio)).append(",");
        sql.append(Sql.getValue(recado.dtCadastro)).append(",");
        sql.append(Sql.getValue(recado.hrCadastro)).append(",");
        sql.append(Sql.getValue(recado.dtLeitura)).append(",");
        sql.append(Sql.getValue(recado.hrLeitura)).append(",");
        sql.append(Sql.getValue(recado.flLido)).append(",");
        sql.append(Sql.getValue(recado.cdStatusEnvio)).append(",");
        sql.append(Sql.getValue(recado.flObrigaResposta)).append(",");
        sql.append(Sql.getValue(recado.flRespostaEnviada)).append(",");
        sql.append(Sql.getValue(recado.flTipoRecado)).append(",");
        sql.append(Sql.getValue(recado.cdRecadoRelacionado)).append(",");
        sql.append(Sql.getValue(BaseDomain.NUCARIMBO_DEFAULT)).append(",");
        sql.append(Sql.getValue(recado.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(recado.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Recado recado = (Recado) domain;
        sql.append(" CDUSUARIOREMETENTE = ").append(Sql.getValue(recado.cdUsuarioRemetente)).append(",");
        sql.append(" CDUSUARIODESTINATARIO = ").append(Sql.getValue(recado.cdUsuarioDestinatario)).append(",");
        sql.append(" CDEMPRESACLIENTE = ").append(Sql.getValue(recado.cdEmpresaCliente)).append(",");
        sql.append(" CDREPRESENTANTECLIENTE = ").append(Sql.getValue(recado.cdRepresentanteCliente)).append(",");
        sql.append(" CDCLIENTE = ").append(Sql.getValue(recado.cdCliente)).append(",");
        sql.append(" DSASSUNTO = ").append(Sql.getValue(recado.dsAssunto)).append(",");
        sql.append(" DSRECADO = ").append(Sql.getValue(recado.dsRecado)).append(",");
        sql.append(" DTENVIO = ").append(Sql.getValue(recado.dtEnvio)).append(",");
        sql.append(" HRENVIO = ").append(Sql.getValue(recado.hrEnvio)).append(",");
        sql.append(" DTCADASTRO = ").append(Sql.getValue(recado.dtCadastro)).append(",");
        sql.append(" HRCADASTRO = ").append(Sql.getValue(recado.hrCadastro)).append(",");
        sql.append(" DTLEITURA = ").append(Sql.getValue(recado.dtLeitura)).append(",");
        sql.append(" HRLEITURA = ").append(Sql.getValue(recado.hrLeitura)).append(",");
        sql.append(" FLLIDO = ").append(Sql.getValue(recado.flLido)).append(",");
        sql.append(" CDSTATUSENVIO = ").append(Sql.getValue(recado.cdStatusEnvio)).append(",");
        sql.append(" FLOBRIGARESPOSTA = ").append(Sql.getValue(recado.flObrigaResposta)).append(",");
        sql.append(" FLRESPOSTAENVIADA = ").append(Sql.getValue(recado.flRespostaEnviada)).append(",");
        sql.append(" FLTIPORECADO = ").append(Sql.getValue(recado.flTipoRecado)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(recado.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(recado.cdUsuario));
    }

    //@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) { }
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException { return null; }

    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        Recado recado = (Recado) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDRECADO = ", recado.cdRecado);
		sqlWhereClause.addAndCondition("cdStatusEnvio = ", recado.cdStatusEnvio);
		sqlWhereClause.addAndCondition("cdUsuarioDestinatario = ", recado.cdUsuarioDestinatario);
		sqlWhereClause.addAndCondition("cdUsuarioRemetente = ", recado.cdUsuarioRemetente);
		sqlWhereClause.addAndCondition("hrCadastro = ", recado.hrCadastro);
		sqlWhereClause.addAndCondition("flLido = ", recado.flLido);
		sqlWhereClause.addAndCondition("dsAssunto = ", recado.dsAssunto);
		sqlWhereClause.addAndCondition("IFNULL(flTipoAlteracao,'I') != ", BaseDomain.FLTIPOALTERACAO_EXCLUIDO);
		sqlWhereClause.addAndCondition("flObrigaResposta = ", recado.flObrigaResposta);
		sqlWhereClause.addAndCondition("flRespostaEnviada = ", recado.flRespostaEnviada);
		sqlWhereClause.addAndCondition("flTipoRecado = ", recado.flTipoRecado);
		sqlWhereClause.addAndCondition("cdEmpresaCliente = ", recado.cdEmpresaCliente);
		sqlWhereClause.addAndCondition("cdCliente = ", recado.cdCliente);
		//--
		sql.append(sqlWhereClause.getSql());
    }

    public int deleteAllByDtEmissao(BaseDomain domain) {
        StringBuffer sql = getSqlBuffer();
        sql.append("delete from ").append(tableName);
        //--
        Recado recado = (Recado) domain;
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("DTCADASTRO <= ", recado.dtCadastro);
		sql.append(sqlWhereClause.getSql());
        //--
        try {
        	return executeUpdate(sql.toString());
        } catch (Throwable e) {
			// Não faz anda, apenas não exclui nenhum registro
		}
        return 0;
    }

    public void deleteAllPurged() throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append("delete from ").append(tableName);
		sql.append(" where flTipoAlteracao = " + Sql.getValue(BaseDomain.FLTIPOALTERACAO_EXCLUIDO));
        //--
        try {
        	executeUpdate(sql.toString());
        } catch (ApplicationException ex) {
			// Não faz anda, apenas não exclui nenhum registro
		}
    }

	public void updateDtHrEnvioRecados() throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("update ").append(tableName).append(" set");
		sql.append(" DTENVIO = ").append(Sql.getValue(DateUtil.getCurrentDate())).append(",");
		sql.append(" HRENVIO = ").append(Sql.getValue(TimeUtil.getCurrentTimeHHMM()));
		sql.append(" where FLTIPOALTERACAO != ").append(Sql.getValue(BaseDomain.FLTIPOALTERACAO_ORIGINAL));
		sql.append(" and FLTIPOALTERACAO != 'E'");
		//--
		executeUpdate(sql.toString());
	}

	public int countRecadoNaoRespondidos(Recado recado) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("SELECT COUNT(QTDRECADOSNAORESPONDIDOS) AS CDRECADO");
		sql.append(" FROM ").append(tableName);
		sql.append(" WHERE FLOBRIGARESPOSTA = ").append(Sql.getValue(recado.flObrigaResposta));
		sql.append(" AND  FLRESPOSTAENVIADA = ").append(Sql.getValue(recado.flRespostaEnviada));
		sql.append(" AND  CDUSUARIODESTINATARIO = ").append(Sql.getValue(recado.cdUsuarioDestinatario));
		return getInt(sql.toString());
	}

	public void updateRespostaEnviada(String cdRecadoRelacionado) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append("UPDATE ").append(tableName).append(" SET");
		sql.append(" FLRESPOSTAENVIADA = ").append(Sql.getValue(ValueUtil.VALOR_SIM)).append(",");
		sql.append(" FLLIDO = ").append(Sql.getValue(ValueUtil.VALOR_SIM));
		sql.append(" WHERE CDRECADO = ").append(Sql.getValue(cdRecadoRelacionado));
		executeUpdate(sql.toString());
	}

}