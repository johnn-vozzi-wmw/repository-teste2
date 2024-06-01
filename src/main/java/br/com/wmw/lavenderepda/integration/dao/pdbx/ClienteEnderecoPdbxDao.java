package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.lang.reflect.Field;
import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import totalcross.sql.ResultSet;

public class ClienteEnderecoPdbxDao extends LavendereCrudPersonDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new ClienteEndereco();
	}

    private static ClienteEnderecoPdbxDao instance;
	

    public ClienteEnderecoPdbxDao() {
        super(ClienteEndereco.TABLE_NAME);
    }
    
    public static ClienteEnderecoPdbxDao getInstance() {
        if (instance == null) {
            instance = new ClienteEnderecoPdbxDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws SQLException {
        ClienteEndereco clienteEndereco = new ClienteEndereco();
        clienteEndereco.rowKey = rs.getString("rowkey");
        clienteEndereco.cdEmpresa = rs.getString("cdEmpresa");
        clienteEndereco.cdRepresentante = rs.getString("cdRepresentante");
        clienteEndereco.cdCliente = rs.getString("cdCliente");
        clienteEndereco.cdEndereco = rs.getString("cdEndereco");
        clienteEndereco.dsLogradouro = rs.getString("dsLogradouro");
        clienteEndereco.nuLogradouro = rs.getString("nuLogradouro");
        clienteEndereco.dsComplemento = rs.getString("dsComplemento");
        clienteEndereco.dsBairro = rs.getString("dsBairro");
        clienteEndereco.dsCep = rs.getString("dsCep");
        clienteEndereco.dsCidade = rs.getString("dsCidade");
        clienteEndereco.dsEstado = rs.getString("dsEstado");
        clienteEndereco.dsPais = rs.getString("dsPais");
        clienteEndereco.dsPontoReferencia = rs.getString("dsPontoreferencia");
        clienteEndereco.dsObservacao = rs.getString("dsObservacao");
        clienteEndereco.flComercial = rs.getString("flComercial");
        clienteEndereco.flEntrega = rs.getString("flEntrega");
        clienteEndereco.flEntregaPadrao = rs.getString("flEntregapadrao");
        clienteEndereco.dsDiaEntrega = rs.getString("dsDiaEntrega");
        clienteEndereco.dsPeriodoEntrega = rs.getString("dsPeriodoEntrega");
        clienteEndereco.dsPeriodoEntregaAlternativo = rs.getString("dsPeriodoEntregaAlternativo");
        clienteEndereco.dsDiaAbertura = rs.getString("dsDiaAbertura");
        clienteEndereco.dsPeriodoAbertura = rs.getString("dsPeriodoAbertura");
        clienteEndereco.flEntregaAgendada = rs.getString("flEntregaAgendada");
        clienteEndereco.cdPeriodoEntrega = rs.getString("cdPeriodoEntrega");
        clienteEndereco.cdTipoEndereco = rs.getString("cdTipoEndereco");
        clienteEndereco.nuCarimbo = rs.getInt("nuCarimbo");
        clienteEndereco.flTipoAlteracao = rs.getString("flTipoAlteracao");
        clienteEndereco.cdUsuario = rs.getString("cdUsuario");
        clienteEndereco.flCobranca = rs.getString("flCobranca");
        clienteEndereco.flCobrancaPadrao = rs.getString("flCobrancaPadrao");
        clienteEndereco.nuCnpj = rs.getString("nuCnpj");
        clienteEndereco.flTipoPessoa = rs.getString("flTipoPessoa");
        return clienteEndereco;
    }
    
	//@Override
	protected BaseDomain populateSummary(BaseDomain domainFilter, ResultSet rs) throws SQLException {
		return null;
	}
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDENDERECO,");
        sql.append(" DSLOGRADOURO,");
        sql.append(" NULOGRADOURO,");
        sql.append(" DSCOMPLEMENTO,");
        sql.append(" DSBAIRRO,");
        sql.append(" DSCEP,");
        sql.append(" DSCIDADE,");
        sql.append(" DSESTADO,");
        sql.append(" DSPAIS,");
        sql.append(" DSPONTOREFERENCIA,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" FLCOMERCIAL,");
        sql.append(" FLENTREGA,");
        sql.append(" FLENTREGAPADRAO,");
        sql.append(" DSDIAENTREGA,");
        sql.append(" DSPERIODOENTREGA,");
        sql.append(" DSPERIODOENTREGAALTERNATIVO,");
        sql.append(" DSDIAABERTURA,");
        sql.append(" DSPERIODOABERTURA,");
        sql.append(" FLENTREGAAGENDADA,");
        sql.append(" CDPERIODOENTREGA,");
        sql.append(" CDTIPOENDERECO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLCOBRANCA,");
        sql.append(" FLCOBRANCAPADRAO,");
        sql.append(" NUCNPJ,");
        sql.append(" FLTIPOPESSOA");
    }
    
	//@Override
	protected void addSelectSummaryColumns(BaseDomain domain, StringBuffer sql) {
	}

    //@Override
    protected void addInsertColumns(StringBuffer sql) throws SQLException {
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDCLIENTE,");
        sql.append(" CDENDERECO,");
        sql.append(" DSLOGRADOURO,");
        sql.append(" NULOGRADOURO,");
        sql.append(" DSCOMPLEMENTO,");
        sql.append(" DSBAIRRO,");
        sql.append(" DSCEP,");
        sql.append(" DSCIDADE,");
        sql.append(" DSESTADO,");
        sql.append(" DSPAIS,");
        sql.append(" DSPONTOREFERENCIA,");
        sql.append(" DSOBSERVACAO,");
        sql.append(" FLCOMERCIAL,");
        sql.append(" FLENTREGA,");
        sql.append(" FLENTREGAPADRAO,");
        sql.append(" DSDIAENTREGA,");
        sql.append(" DSPERIODOENTREGA,");
        sql.append(" DSPERIODOENTREGAALTERNATIVO,");
        sql.append(" DSDIAABERTURA,");
        sql.append(" DSPERIODOABERTURA,");
        sql.append(" FLENTREGAAGENDADA,");
        sql.append(" CDPERIODOENTREGA,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO,");
        sql.append(" FLCOBRANCA,");
        sql.append(" FLCOBRANCAPADRAO,");
        sql.append(" NUCNPJ,");
        sql.append(" FLTIPOPESSOA");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws SQLException {
        ClienteEndereco clienteEndereco = (ClienteEndereco) domain;
        sql.append(Sql.getValue(clienteEndereco.cdEmpresa)).append(",");
        sql.append(Sql.getValue(clienteEndereco.cdRepresentante)).append(",");
        sql.append(Sql.getValue(clienteEndereco.cdCliente)).append(",");
        sql.append(Sql.getValue(clienteEndereco.cdEndereco)).append(",");
        sql.append(Sql.getValue(clienteEndereco.dsLogradouro)).append(",");
        sql.append(Sql.getValue(clienteEndereco.nuLogradouro)).append(",");
        sql.append(Sql.getValue(clienteEndereco.dsComplemento)).append(",");
        sql.append(Sql.getValue(clienteEndereco.dsBairro)).append(",");
        sql.append(Sql.getValue(clienteEndereco.dsCep)).append(",");
        sql.append(Sql.getValue(clienteEndereco.dsCidade)).append(",");
        sql.append(Sql.getValue(clienteEndereco.dsEstado)).append(",");
        sql.append(Sql.getValue(clienteEndereco.dsPais)).append(",");
        sql.append(Sql.getValue(clienteEndereco.dsPontoReferencia)).append(",");
        sql.append(Sql.getValue(clienteEndereco.dsObservacao)).append(",");
        sql.append(Sql.getValue(clienteEndereco.flComercial)).append(",");
        sql.append(Sql.getValue(clienteEndereco.flEntrega)).append(",");
        sql.append(Sql.getValue(clienteEndereco.flEntregaPadrao)).append(",");
        sql.append(Sql.getValue(clienteEndereco.dsDiaEntrega)).append(",");
        sql.append(Sql.getValue(clienteEndereco.dsPeriodoEntrega)).append(",");
        sql.append(Sql.getValue(clienteEndereco.dsPeriodoEntregaAlternativo)).append(",");
        sql.append(Sql.getValue(clienteEndereco.dsDiaAbertura)).append(",");
        sql.append(Sql.getValue(clienteEndereco.dsPeriodoAbertura)).append(",");
        sql.append(Sql.getValue(clienteEndereco.flEntregaAgendada)).append(",");
        sql.append(Sql.getValue(clienteEndereco.cdPeriodoEntrega)).append(",");
        sql.append(Sql.getValue(clienteEndereco.nuCarimbo)).append(",");
        sql.append(Sql.getValue(clienteEndereco.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(clienteEndereco.cdUsuario)).append(",");
        sql.append(Sql.getValue(clienteEndereco.flCobranca)).append(",");
        sql.append(Sql.getValue(clienteEndereco.flCobrancaPadrao)).append(",");
        sql.append(Sql.getValue(clienteEndereco.nuCnpj)).append(",");
        sql.append(Sql.getValue(clienteEndereco.flTipoPessoa));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws SQLException {
    	ClienteEndereco clienteEndereco = (ClienteEndereco) domain;
        sql.append(" DSLOGRADOURO = ").append(Sql.getValue(clienteEndereco.dsLogradouro)).append(",");
        sql.append(" NULOGRADOURO = ").append(Sql.getValue(clienteEndereco.nuLogradouro)).append(",");
        sql.append(" DSCOMPLEMENTO = ").append(Sql.getValue(clienteEndereco.dsComplemento)).append(",");
        sql.append(" DSBAIRRO = ").append(Sql.getValue(clienteEndereco.dsBairro)).append(",");
        sql.append(" DSCEP = ").append(Sql.getValue(clienteEndereco.dsCep)).append(",");
        sql.append(" DSCIDADE = ").append(Sql.getValue(clienteEndereco.dsCidade)).append(",");
        sql.append(" DSESTADO = ").append(Sql.getValue(clienteEndereco.dsEstado)).append(",");
        sql.append(" DSPAIS = ").append(Sql.getValue(clienteEndereco.dsPais)).append(",");
        sql.append(" DSPONTOREFERENCIA = ").append(Sql.getValue(clienteEndereco.dsPontoReferencia)).append(",");
        sql.append(" DSOBSERVACAO = ").append(Sql.getValue(clienteEndereco.dsObservacao)).append(",");
        sql.append(" FLCOMERCIAL = ").append(Sql.getValue(clienteEndereco.flComercial)).append(",");
        sql.append(" FLENTREGA = ").append(Sql.getValue(clienteEndereco.flEntrega)).append(",");
        sql.append(" FLENTREGAPADRAO = ").append(Sql.getValue(clienteEndereco.flEntregaPadrao)).append(",");
        sql.append(" DSDIAENTREGA = ").append(Sql.getValue(clienteEndereco.dsDiaEntrega)).append(",");
        sql.append(" DSPERIODOENTREGA = ").append(Sql.getValue(clienteEndereco.dsPeriodoEntrega)).append(",");
        sql.append(" DSPERIODOENTREGAALTERNATIVO = ").append(Sql.getValue(clienteEndereco.dsPeriodoEntregaAlternativo)).append(",");
        sql.append(" DSDIAABERTURA = ").append(Sql.getValue(clienteEndereco.dsDiaAbertura)).append(",");
        sql.append(" DSPERIODOABERTURA = ").append(Sql.getValue(clienteEndereco.dsPeriodoAbertura)).append(",");
        sql.append(" FLENTREGAAGENDADA = ").append(Sql.getValue(clienteEndereco.flEntregaAgendada)).append(",");
        sql.append(" CDPERIODOENTREGA = ").append(Sql.getValue(clienteEndereco.cdPeriodoEntrega)).append(",");
        sql.append(" NUCARIMBO = ").append(Sql.getValue(clienteEndereco.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(clienteEndereco.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(clienteEndereco.cdUsuario)).append(",");
        sql.append(" FLCOBRANCA = ").append(Sql.getValue(clienteEndereco.flCobranca)).append(",");
        sql.append(" FLCOBRANCAPADRAO = ").append(Sql.getValue(clienteEndereco.flCobrancaPadrao)).append(",");
        sql.append(" NUCNPJ = ").append(Sql.getValue(clienteEndereco.nuCnpj)).append(",");
        sql.append(" FLTIPOPESSOA = ").append(Sql.getValue(clienteEndereco.flTipoPessoa));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws SQLException {
    	ClienteEndereco clienteEndereco = (ClienteEndereco) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", clienteEndereco.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", clienteEndereco.cdRepresentante);
		sqlWhereClause.addAndCondition("CDCLIENTE = ", clienteEndereco.cdCliente);
		sqlWhereClause.addAndCondition("CDENDERECO = ", clienteEndereco.cdEndereco);
		sqlWhereClause.addAndCondition("FLENTREGA = ", clienteEndereco.flEntrega);
		sqlWhereClause.addAndCondition("FLENTREGAPADRAO = ", clienteEndereco.flEntregaPadrao);
		sqlWhereClause.addAndCondition("FLCOBRANCA = ", clienteEndereco.flCobranca);
		sqlWhereClause.addAndCondition("FLCOBRANCAPADRAO = ", clienteEndereco.flCobrancaPadrao);
		//--
		sql.append(sqlWhereClause.getSql());
    }
 
    public void aplicaAlteracoesEndereco(ClienteEndereco clienteEnderecoOriginal, ClienteEndereco clienteEndereco) {
    	String column = null;
		try {
			StringBuffer sql = getSqlBuffer();
			sql.append(" UPDATE TBLVPCLIENTEENDERECO SET ");
			String[] columns = StringUtil.split(LavenderePdaConfig.aplicaAlteracoesCadastroClienteEnderecoAutomaticamente, ';');
			int size = columns.length;
			for (int i = 0; i < size; i++) {
				column = columns[i];
				Field field = ClienteEndereco.class.getDeclaredField(column);
				sql.append(column).append(" = ").append(Sql.getValue(field.get(clienteEndereco)));
				if (i + 1 < size) {
					sql.append(", ");
				}
			}
			addWhereByExample(clienteEnderecoOriginal, sql);
			executeUpdate(sql.toString());
		} catch (Throwable e) {
			throw new ValidationException(MessageUtil.getMessage(Messages.MSG_ERRO_ATUALIZAR_ENDERECO_CLIENTE, column));
		}
    }
    
    public int countClienteEnderecoEntregaOuCobranca(ClienteEndereco clienteEndereco, boolean isEnderecoCobranca) throws SQLException {
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT count(*) as qtde FROM ");
    	sql.append(tableName);
        sql.append(" tb ");
        sql.append(" WHERE CDEMPRESA = '").append(clienteEndereco.cdEmpresa).append("'");
        sql.append(" AND CDREPRESENTANTE = '").append(clienteEndereco.cdRepresentante).append("'");
        sql.append(" AND CDCLIENTE = '").append(clienteEndereco.cdCliente).append("'");
        if (isEnderecoCobranca) {        	
        	sql.append(" AND (FLCOBRANCA = '").append(clienteEndereco.flCobranca).append("'");
        	sql.append(" OR FLCOBRANCAPADRAO = '").append(clienteEndereco.flCobrancaPadrao).append("')");
        } else {
        	sql.append(" AND (FLENTREGA = '").append(clienteEndereco.flEntrega).append("'");
        	sql.append(" OR FLENTREGAPADRAO = '").append(clienteEndereco.flEntregaPadrao).append("')");
        }
        return getInt(sql.toString());
    }
    
}