package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.NovoClienteAna;
import br.com.wmw.lavenderepda.business.domain.Prospect;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.FotoNovoClienteService;
import br.com.wmw.lavenderepda.business.service.NovoCliEnderecoService;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.sql.Types;
import totalcross.util.Vector;

public class NovoClienteAnaDbxDao extends LavendereCrudPersonDbxDao {

	private static NovoClienteAnaDbxDao instance = null;

	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new NovoClienteAna();
	}


    public NovoClienteAnaDbxDao() {
        super(NovoClienteAna.TABLE_NAME);
    }
    
    public static NovoClienteAnaDbxDao getInstance() {
        if (instance == null) {
            instance = new NovoClienteAnaDbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        NovoClienteAna novoClienteAna = new NovoClienteAna();
        novoClienteAna.rowKey = rs.getString("rowkey");
        novoClienteAna.cdEmpresa = rs.getString("cdEmpresa");
        novoClienteAna.cdRepresentante = rs.getString("cdRepresentante");
        novoClienteAna.flOrigemNovoCliente = rs.getString("flOrigemNovoCliente");
        novoClienteAna.cdNovoCliente = rs.getString("cdNovoCliente");
        novoClienteAna.cdTipoAnalise = rs.getString("cdTipoAnalise");
        novoClienteAna.cdStatusAnalise = rs.getString("cdStatusAnalise");
        novoClienteAna.cdMotivoReprovacao = rs.getString("cdMotivoReprovacao");
        novoClienteAna.vlLimiteAprovado = ValueUtil.round(rs.getDouble("vlLimiteAprovado"));
        novoClienteAna.cdCondicaoComercial = rs.getString("cdCondicaoComercial");
        novoClienteAna.dsObservacao = rs.getString("dsObservacao");
        novoClienteAna.cdUsuarioEdicao = rs.getString("cdUsuarioEdicao");
        novoClienteAna.nmUsuarioEdicao = rs.getString("nmUsuarioEdicao");
        novoClienteAna.dtEdicaoUsuario = rs.getDate("dtEdicaoUsuario");
        novoClienteAna.hrEdicaoUsuario = rs.getString("hrEdicaoUsuario");
        novoClienteAna.dtTransmissaoPda = rs.getDate("dtTransmissaoPda");
        novoClienteAna.hrTransmissaoPda = rs.getString("hrTransmissaoPda");
        novoClienteAna.dtCadastro = rs.getDate("dtCadastro");
        novoClienteAna.hrCadastro = rs.getString("hrCadastro");
        novoClienteAna.flCnpjRecorrente = rs.getString("flCnpjRecorrente");
        novoClienteAna.flNaoPossuiCobertura = rs.getString("flNaoPossuiCobertura");
        novoClienteAna.nuCarimbo = rs.getInt("nuCarimbo");
        novoClienteAna.flTipoAlteracao = rs.getString("flTipoAlteracao");
        novoClienteAna.nuCnpj = rs.getString("nuCnpj");
        novoClienteAna.nmRazaoSocial = rs.getString("nmRazaoSocial");
        novoClienteAna.flTipoPessoa = rs.getString("flTipoPessoa");
        novoClienteAna.flPermiteMultiplosEnderecos = rs.getString("flPermiteMultiplosEnderecos");
        novoClienteAna.dtAlteracao = rs.getDate("dtAlteracao");
        novoClienteAna.hrAlteracao = rs.getString("hrAlteracao");
        novoClienteAna.cdUsuario = rs.getString("cdUsuario");
        return novoClienteAna;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMNOVOCLIENTE,");
        sql.append(" CDNOVOCLIENTE,");
        sql.append(" CDTIPOANALISE,");
        sql.append(" CDSTATUSANALISE,");
        sql.append(" CDMOTIVOREPROVACAO,");
        sql.append(" VLLIMITEAPROVADO,");
        sql.append(" CDCONDICAOCOMERCIAL,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" CDUSUARIOEDICAO,");
        sql.append(" NMUSUARIOEDICAO,");
        sql.append(" DTEDICAOUSUARIO,");
        sql.append(" HREDICAOUSUARIO,");
        sql.append(" DTTRANSMISSAOPDA,");
        sql.append(" HRTRANSMISSAOPDA,");
        sql.append(" DTCADASTRO,");
        sql.append(" HRCADASTRO,");
        sql.append(" FLCNPJRECORRENTE,");
        sql.append(" FLNAOPOSSUICOBERTURA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" NMRAZAOSOCIAL,");
        sql.append(" NUCNPJ,");
        sql.append(" FLTIPOPESSOA,");
        sql.append(" FLPERMITEMULTIPLOSENDERECOS,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMNOVOCLIENTE,");
        sql.append(" CDNOVOCLIENTE,");
        sql.append(" CDTIPOANALISE,");
        sql.append(" CDSTATUSANALISE,");
        sql.append(" CDMOTIVOREPROVACAO,");
        sql.append(" VLLIMITEAPROVADO,");
        sql.append(" CDCONDICAOCOMERCIAL,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" CDUSUARIOEDICAO,");
        sql.append(" NMUSUARIOEDICAO,");
        sql.append(" DTEDICAOUSUARIO,");
        sql.append(" HREDICAOUSUARIO,");
        sql.append(" DTTRANSMISSAOPDA,");
        sql.append(" HRTRANSMISSAOPDA,");
        sql.append(" DTCADASTRO,");
        sql.append(" HRCADASTRO,");
        sql.append(" FLCNPJRECORRENTE,");
        sql.append(" FLNAOPOSSUICOBERTURA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" DTALTERACAO,");
        sql.append(" HRALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) {
        NovoClienteAna novoClienteAna = (NovoClienteAna) domain;
        sql.append(Sql.getValue(novoClienteAna.cdEmpresa)).append(",");
        sql.append(Sql.getValue(novoClienteAna.cdRepresentante)).append(",");
        sql.append(Sql.getValue(novoClienteAna.flOrigemNovoCliente)).append(",");
        sql.append(Sql.getValue(novoClienteAna.cdNovoCliente)).append(",");
        sql.append(Sql.getValue(novoClienteAna.cdTipoAnalise)).append(",");
        sql.append(Sql.getValue(novoClienteAna.cdStatusAnalise)).append(",");
        sql.append(Sql.getValue(novoClienteAna.cdMotivoReprovacao)).append(",");
        sql.append(Sql.getValue(novoClienteAna.vlLimiteAprovado)).append(",");
        sql.append(Sql.getValue(novoClienteAna.cdCondicaoComercial)).append(",");
        sql.append(Sql.getValue(novoClienteAna.dsObservacao)).append(",");
        sql.append(Sql.getValue(novoClienteAna.cdUsuarioEdicao)).append(",");
        sql.append(Sql.getValue(novoClienteAna.nmUsuarioEdicao)).append(",");
        sql.append(Sql.getValue(novoClienteAna.dtEdicaoUsuario)).append(",");
        sql.append(Sql.getValue(novoClienteAna.hrEdicaoUsuario)).append(",");
        sql.append(Sql.getValue(novoClienteAna.dtTransmissaoPda)).append(",");
        sql.append(Sql.getValue(novoClienteAna.hrTransmissaoPda)).append(",");
        sql.append(Sql.getValue(novoClienteAna.dtCadastro)).append(",");
        sql.append(Sql.getValue(novoClienteAna.hrCadastro)).append(",");
        sql.append(Sql.getValue(novoClienteAna.flCnpjRecorrente)).append(",");
        sql.append(Sql.getValue(novoClienteAna.flNaoPossuiCobertura)).append(",");
        sql.append(Sql.getValue(novoClienteAna.nuCarimbo)).append(",");
        sql.append(Sql.getValue(novoClienteAna.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(novoClienteAna.dtAlteracao)).append(",");
        sql.append(Sql.getValue(novoClienteAna.hrAlteracao)).append(",");
        sql.append(Sql.getValue(novoClienteAna.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) {
        NovoClienteAna novoClienteAna = (NovoClienteAna) domain;
        sql.append(" CDTIPOANALISE = ").append(Sql.getValue(novoClienteAna.cdTipoAnalise)).append(",");
        sql.append(" CDSTATUSANALISE = ").append(Sql.getValue(novoClienteAna.cdStatusAnalise)).append(",");
        sql.append(" CDMOTIVOREPROVACAO = ").append(Sql.getValue(novoClienteAna.cdMotivoReprovacao)).append(",");
        sql.append(" VLLIMITEAPROVADO = ").append(Sql.getValue(novoClienteAna.vlLimiteAprovado)).append(",");
        sql.append(" CDCONDICAOCOMERCIAL = ").append(Sql.getValue(novoClienteAna.cdCondicaoComercial)).append(",");
        sql.append(" DSOBSERVACAO = ").append(Sql.getValue(novoClienteAna.dsObservacao)).append(",");
        sql.append(" CDUSUARIOEDICAO = ").append(Sql.getValue(novoClienteAna.cdUsuarioEdicao)).append(",");
        sql.append(" NMUSUARIOEDICAO = ").append(Sql.getValue(novoClienteAna.nmUsuarioEdicao)).append(",");
        sql.append(" DTEDICAOUSUARIO = ").append(Sql.getValue(novoClienteAna.dtEdicaoUsuario)).append(",");
        sql.append(" HREDICAOUSUARIO = ").append(Sql.getValue(novoClienteAna.hrEdicaoUsuario)).append(",");
        sql.append(" DTTRANSMISSAOPDA = ").append(Sql.getValue(novoClienteAna.dtTransmissaoPda)).append(",");
        sql.append(" HRTRANSMISSAOPDA = ").append(Sql.getValue(novoClienteAna.hrTransmissaoPda)).append(",");
        sql.append(" DTCADASTRO = ").append(Sql.getValue(novoClienteAna.dtCadastro)).append(",");
        sql.append(" HRCADASTRO = ").append(Sql.getValue(novoClienteAna.hrCadastro)).append(",");
        sql.append(" FLCNPJRECORRENTE = ").append(Sql.getValue(novoClienteAna.flCnpjRecorrente)).append(",");
        sql.append(" FLNAOPOSSUICOBERTURA = ").append(Sql.getValue(novoClienteAna.flNaoPossuiCobertura)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(novoClienteAna.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(novoClienteAna.flTipoAlteracao)).append(",");
        sql.append(" DTALTERACAO = ").append(Sql.getValue(novoClienteAna.dtAlteracao)).append(",");
        sql.append(" HRALTERACAO = ").append(Sql.getValue(novoClienteAna.hrAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(novoClienteAna.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) {
        NovoClienteAna novoClienteAna = (NovoClienteAna) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", novoClienteAna.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", novoClienteAna.cdRepresentante);
		sqlWhereClause.addAndCondition("FLORIGEMNOVOCLIENTE = ", novoClienteAna.flOrigemNovoCliente);
		sqlWhereClause.addAndCondition("CDNOVOCLIENTE = ", novoClienteAna.cdNovoCliente);
		sqlWhereClause.addAndCondition("DTCADASTRO < ", novoClienteAna.dtCadastro);
		//--
		sql.append(sqlWhereClause.getSql());
    }

	public void deleteAllByDtLimite(NovoClienteAna novoClienteAna) throws SQLException {
		Vector list = findAllByExample(novoClienteAna);
		for (int i = 0; i < list.size(); i++) {
			NovoClienteAna novoCliAna = (NovoClienteAna) list.items[i];
			NovoCliente novoCliente = new NovoCliente();
			novoCliente.cdEmpresa = novoCliAna.cdEmpresa;
			novoCliente.cdRepresentante = novoCliAna.cdRepresentante;
			novoCliente.flOrigemNovoCliente = novoCliAna.flOrigemNovoCliente;
			novoCliente.cdNovoCliente = novoCliAna.cdNovoCliente;
			String flOculto = NovoClientePdbxDao.getInstance().findColumnByRowKey(novoCliente.getRowKey(), NovoCliente.NMCOLUNA_FLOCULTO);
			if (ValueUtil.VALOR_SIM.equals(flOculto)) {
				if (LavenderePdaConfig.isUsaCadastroFotoNovoCliente()) {
					FotoNovoClienteService.getInstance().excluiFotosNovoCliente(novoCliente);
				}
				if (LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente()) {
					NovoCliEnderecoService.getInstance().deleteRegistrosNovoCliEndereco(novoCliente);
				}
				delete(novoCliente);
				ClienteService.getInstance().deleteNovoCliente(novoCliente);
			} else {
				delete(novoCliente);
				try {
					NovoClientePdbxDao.getInstance().updateColumn(novoCliente.getRowKey(), NovoCliente.NMCOLUNA_FLEMANALISE, ValueUtil.VALOR_NAO, Types.VARCHAR);
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
			}
		}
	}
 
	
	public boolean hasNovoClienteAnaPendenteEdicao() throws SQLException {
		StringBuffer sql = getSqlBuffer();
    	sql.append("select count(cdEmpresa) as count");
    	sql.append(" from ").append(tableName).append(" tb ");
    	sql.append(" LEFT OUTER JOIN tblvpstatusanalisecliente status ON ");
		sql.append(" tb.cdStatusAnalise = status.cdStatusAnalise ");
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("status.flConclusao = ", ValueUtil.VALOR_NAO);
		sqlWhereClause.addAndCondition("status.flPermiteEdicao = ", ValueUtil.VALOR_SIM);
		sql.append(sqlWhereClause.getSql());
		return getInt(sql.toString()) > 0;
	}
	
	public Vector findAllNovoClienteAna(NovoCliente novoCliente) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append("select ");
    	addFindAllNovoClienteAnaColumns(sql);
    	sql.append(" from ").append(tableName).append(" tb ");
    	sql.append(" LEFT OUTER JOIN tblvpnovocliente novoCli ON ");
    	sql.append(" tb.cdNovoCliente = novoCli.cdNovoCliente");
    	sql.append(" AND tb.cdEmpresa = novoCli.cdEmpresa ");
    	sql.append(" AND tb.cdRepresentante = novoCli.cdRepresentante ");
    	sql.append(" AND tb.flOrigemNovoCliente = novoCli.flOrigemNovoCliente ");
    	sql.append(" LEFT OUTER JOIN tblvpcliente cli ON ");
    	sql.append(" tb.cdEmpresa = cli.cdEmpresa ");
    	sql.append(" AND tb.cdRepresentante = cli.cdRepresentante ");
    	sql.append(" AND tb.nuCnpj = cli.nuCnpj ");
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		sql.append(" INNER JOIN tblvpsupervisorrep rep ON ");
    		sql.append(" tb.cdEmpresa = rep.cdEmpresa ");
    		sql.append(" AND tb.cdRepresentante = rep.cdRepresentante ");
    	}
    	if (novoCliente.novoClienteAna.ckNaoFinalizados || novoCliente.novoClienteAna.ckApenasPendencias) {
    		sql.append(" LEFT OUTER JOIN tblvpstatusanalisecliente status ON ");
    		sql.append(" tb.cdStatusAnalise = status.cdStatusAnalise ");
    	}
    	addWhereColumns(sql, novoCliente);
    	if (ValueUtil.isNotEmpty(novoCliente.sortAtributte)) {
    		sql.append(" order by ");
    		sql.append(" tb.");
    		sql.append(novoCliente.sortAtributte);
    		sql.append(ValueUtil.VALOR_SIM.equals(novoCliente.sortAsc) ? " ASC" : " DESC");
    	}
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		Vector novoCliList = new Vector();
    		while (rs.next()) {
    			novoCliList.addElement(populateFindAllNovoClienteAna(rs));
    		}
    		return novoCliList;
    	}
    }

    public Vector findAllNovoClienteAna(Prospect prospect) throws SQLException {
        StringBuffer sql = getSqlBuffer();
        sql.append("select ");
        addFindAllNovoClienteAnaColumns(sql);
        sql.append(" from ").append(tableName).append(" tb ");
        sql.append(" LEFT JOIN TBLVPPROSPECT prosp ON ");
        sql.append(" tb.cdNovoCliente = prosp.cdProspect");
        sql.append(" AND tb.cdEmpresa = prosp.cdEmpresa ");
        sql.append(" AND tb.cdRepresentante = prosp.cdRepresentante ");
        sql.append(" AND tb.flOrigemNovoCliente = prosp.flOrigemProspect ");
        if (SessionLavenderePda.isUsuarioSupervisor()) {
            sql.append(" INNER JOIN tblvpsupervisorrep rep ON ");
            sql.append(" tb.cdEmpresa = rep.cdEmpresa ");
            sql.append(" AND tb.cdRepresentante = rep.cdRepresentante ");
        }
        if (prospect.novoClienteAna.ckNaoFinalizados || prospect.novoClienteAna.ckApenasPendencias) {
            sql.append(" LEFT OUTER JOIN tblvpstatusanalisecliente status ON ");
            sql.append(" tb.cdStatusAnalise = status.cdStatusAnalise ");
        }
        addWhereColumns(sql, prospect);
        if (ValueUtil.isNotEmpty(prospect.sortAtributte)) {
            sql.append(" order by ");
            sql.append(" tb.");
            sql.append(prospect.sortAtributte);
            sql.append(ValueUtil.VALOR_SIM.equals(prospect.sortAsc) ? " ASC" : " DESC");
        }
        try (Statement st = getCurrentDriver().getStatement();
        		ResultSet rs = st.executeQuery(sql.toString())) {
            Vector prospectList = new Vector();
            while (rs.next()) {
                prospectList.addElement(populateFindAllNovoClienteAnaProspect(rs));
            }
            return prospectList;
        }
    }
    
    private void addWhereColumns(StringBuffer sql, NovoCliente novoCliente) throws SQLException {
    	SqlWhereClause sqlWhereClause = new SqlWhereClause();
    	sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", novoCliente.cdEmpresa);
    	sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", novoCliente.cdRepresentante);
		sqlWhereClause.addAndCondition("tb.dtCadastro >= ", novoCliente.dtCadastroInicial);
		sqlWhereClause.addAndCondition("tb.dtCadastro <= ", novoCliente.dtCadastroFinal);
		sqlWhereClause.addAndCondition("tb.dtEdicaoUsuario >= ", novoCliente.novoClienteAna.dtEdicaoUsuarioInicial);
		sqlWhereClause.addAndCondition("tb.dtEdicaoUsuario <= ", novoCliente.novoClienteAna.dtEdicaoUsuarioFinal);
		sqlWhereClause.addAndCondition("tb.cdStatusAnalise = ", novoCliente.novoClienteAna.cdStatusAnalise);
		if (ValueUtil.isNotEmpty(novoCliente.novoClienteAna.dsFiltro)) {
			String dsFiltro = novoCliente.novoClienteAna.dsFiltro;
			sqlWhereClause.addStartAndMultipleCondition();
			sqlWhereClause.addOrCondition("tb.nuCnpj = ", dsFiltro);
			sqlWhereClause.addOrLikeCondition("tb.nmRazaoSocial", dsFiltro);
			sqlWhereClause.addOrLikeCondition("novoCli.nmFantasia", dsFiltro);
			sqlWhereClause.addOrCondition("tb.cdNovoCliente = ", dsFiltro);
			sqlWhereClause.addOrCondition("cli.cdCliente = ", dsFiltro);
			sqlWhereClause.addEndMultipleCondition();
		}
		if (novoCliente.novoClienteAna.ckNaoFinalizados) {
			sqlWhereClause.addAndCondition("status.flConclusao = ", ValueUtil.VALOR_NAO);
		}
		if (novoCliente.novoClienteAna.ckApenasPendencias) {
			sqlWhereClause.addAndCondition("status.flConclusao = ", ValueUtil.VALOR_NAO);
			sqlWhereClause.addAndCondition("status.flPermiteEdicao = ", ValueUtil.VALOR_SIM);
		}
		sql.append(sqlWhereClause.getSql());
	}

    private void addWhereColumns(StringBuffer sql, Prospect prospect) throws SQLException {
        SqlWhereClause sqlWhereClause = new SqlWhereClause();
        sqlWhereClause.addAndCondition("tb.CDEMPRESA = ", prospect.cdEmpresa);
        sqlWhereClause.addAndCondition("tb.CDREPRESENTANTE = ", prospect.cdRepresentante);
        sqlWhereClause.addAndCondition("tb.dtEdicaoUsuario >= ", prospect.novoClienteAna.dtEdicaoUsuarioInicial);
        sqlWhereClause.addAndCondition("tb.dtEdicaoUsuario <= ", prospect.novoClienteAna.dtEdicaoUsuarioFinal);
        sqlWhereClause.addAndCondition("tb.cdStatusAnalise = ", prospect.novoClienteAna.cdStatusAnalise);
        if (ValueUtil.isNotEmpty(prospect.novoClienteAna.dsFiltro)) {
            String dsFiltro = prospect.novoClienteAna.dsFiltro;
            sqlWhereClause.addStartAndMultipleCondition();
            sqlWhereClause.addOrCondition("tb.nuCnpj = ", dsFiltro);
            sqlWhereClause.addOrLikeCondition("tb.nmRazaoSocial", dsFiltro);
            sqlWhereClause.addOrLikeCondition("prosp.nmFantasia", dsFiltro);
            sqlWhereClause.addOrCondition("tb.cdNovoCliente = ", dsFiltro);
            sqlWhereClause.addEndMultipleCondition();
        }
        if (prospect.novoClienteAna.ckNaoFinalizados) {
            sqlWhereClause.addAndCondition("status.flConclusao = ", ValueUtil.VALOR_NAO);
        }
        if (prospect.novoClienteAna.ckApenasPendencias) {
            sqlWhereClause.addAndCondition("status.flConclusao = ", ValueUtil.VALOR_NAO);
            sqlWhereClause.addAndCondition("status.flPermiteEdicao = ", ValueUtil.VALOR_SIM);
        }
        sql.append(sqlWhereClause.getSql());
    }

    public void addFindAllNovoClienteAnaColumns(StringBuffer sql) throws java.sql.SQLException {
        boolean cadastroEmDuasEtapas = LavenderePdaConfig.isValidaCadastroDuasEtapas();
        sql.append(" tb.rowkey,");
        sql.append(" tb.CDEMPRESA,");
        sql.append(" tb.CDREPRESENTANTE,");
        sql.append(" tb.FLORIGEMNOVOCLIENTE,");
        sql.append(" tb.CDNOVOCLIENTE,");
        if (cadastroEmDuasEtapas) {
            sql.append(" COALESCE(prosp.NUCNPJ, tb.NUCNPJ) AS NUCNPJ,");
            sql.append(" COALESCE(prosp.DTCADASTRO, tb.DTCADASTRO) AS DTCADASTRO,");
            sql.append(" COALESCE(prosp.NMRAZAOSOCIAL, tb.NMRAZAOSOCIAL) AS NMRAZAOSOCIAL,");
        } else {
            sql.append(" tb.NUCNPJ,");
            sql.append(" tb.DTCADASTRO,");
            sql.append(" novoCli.CDSTATUSNOVOCLIENTE,");
            sql.append(" tb.NMRAZAOSOCIAL,");
        }
        sql.append(" tb.CDSTATUSANALISE,");
        sql.append(" tb.FLCNPJRECORRENTE,");
        sql.append(" tb.FLNAOPOSSUICOBERTURA,");
        sql.append(" tb.DTEDICAOUSUARIO ");
    }
    
    protected BaseDomain populateFindAllNovoClienteAna(ResultSet rs) throws java.sql.SQLException {
        NovoCliente novocli = new NovoCliente();
        novocli.rowKey = rs.getString("rowkey");
        novocli.cdEmpresa = rs.getString("cdEmpresa");
        novocli.cdRepresentante = rs.getString("cdRepresentante");
        novocli.flOrigemNovoCliente = rs.getString("flOrigemnovocliente");
        novocli.cdNovoCliente = rs.getString("cdNovoCliente");
        novocli.novoClienteAna.nuCnpj = rs.getString("nuCnpj");
        novocli.novoClienteAna.nmRazaoSocial = rs.getString("nmRazaoSocial");
        novocli.novoClienteAna.dtCadastro = rs.getDate("dtCadastro");
        novocli.novoClienteAna.dtEdicaoUsuario = rs.getDate("dtEdicaoUsuario");
        novocli.novoClienteAna.flCnpjRecorrente = rs.getString("flCnpjRecorrente");
        novocli.novoClienteAna.flNaoPossuiCobertura = rs.getString("flNaoPossuiCobertura");
        novocli.novoClienteAna.cdStatusAnalise = rs.getString("cdStatusAnalise");
        return novocli;
    }

    protected BaseDomain populateFindAllNovoClienteAnaProspect(ResultSet rs) throws java.sql.SQLException {
        Prospect prospect = new Prospect();
        prospect.rowKey = rs.getString("rowkey");
        prospect.cdEmpresa = rs.getString("cdEmpresa");
        prospect.cdRepresentante = rs.getString("cdRepresentante");
        prospect.flOrigemProspect = rs.getString("flOrigemNovoCliente");
        prospect.cdProspect = rs.getString("cdNovoCliente");
        prospect.novoClienteAna = new NovoClienteAna();
        prospect.novoClienteAna.nuCnpj = rs.getString("nuCnpj");
        prospect.novoClienteAna.nmRazaoSocial = rs.getString("nmRazaoSocial");
        prospect.novoClienteAna.dtCadastro = rs.getDate("dtCadastro");
        prospect.novoClienteAna.dtEdicaoUsuario = rs.getDate("dtEdicaoUsuario");
        prospect.novoClienteAna.flCnpjRecorrente = rs.getString("flCnpjRecorrente");
        prospect.novoClienteAna.flNaoPossuiCobertura = rs.getString("flNaoPossuiCobertura");
        prospect.novoClienteAna.cdStatusAnalise = rs.getString("cdStatusAnalise");
        return prospect;
    }
}