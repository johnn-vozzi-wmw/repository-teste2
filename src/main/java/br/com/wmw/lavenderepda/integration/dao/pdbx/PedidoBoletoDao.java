package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.PedidoBoleto;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.sys.InvalidNumberException;
import totalcross.util.BigDecimal;
import totalcross.util.Date;

public class PedidoBoletoDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PedidoBoleto();
	}

    private static PedidoBoletoDao instance;
	
	public static boolean houveRecebimentoBoletoBackground;
	public static String erroOcorridoAtualizacao;

    public PedidoBoletoDao() {
        super(PedidoBoleto.TABLE_NAME);
    }
    
    public static PedidoBoletoDao getInstance() {
        if (instance == null) {
            instance = new PedidoBoletoDao();
        }
        return instance;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        PedidoBoleto pedidoBoleto = new PedidoBoleto();
        pedidoBoleto.rowKey = rs.getString("rowkey");
        pedidoBoleto.cdEmpresa = rs.getString("cdEmpresa");
        pedidoBoleto.cdRepresentante = rs.getString("cdRepresentante");
        pedidoBoleto.flOrigemPedido = rs.getString("flOrigemPedido");
        pedidoBoleto.nuPedido = rs.getString("nuPedido");
        pedidoBoleto.nuSequenciaBoletoPedido = rs.getInt("nuSequenciaBoletoPedido");
        pedidoBoleto.cdPagamentoPedido = rs.getString("cdPagamentoPedido");
        pedidoBoleto.cdBoletoConfig = rs.getString("cdBoletoConfig");
        pedidoBoleto.cdBarras = rs.getString("cdBarras");
        pedidoBoleto.dsLinhasDigitavel = rs.getString("dsLinhasDigitavel");
        pedidoBoleto.dtVencimento = rs.getDate("dtVencimento");
        pedidoBoleto.nuAgenciaCodigoCedente = rs.getString("nuAgenciaCodigoCedente");
        pedidoBoleto.dtDocumento = rs.getDate("dtDocumento");
        pedidoBoleto.nuDocumento = rs.getBigDecimal("nuDocumento");
        pedidoBoleto.vlBoleto = ValueUtil.round(rs.getDouble("vlBoleto"));
        pedidoBoleto.nuNossoNumero = rs.getString("nuNossoNumero");
        pedidoBoleto.dsLocalPagamento = rs.getString("dsLocalPagamento");
        pedidoBoleto.nuCarteira = rs.getString("nuCarteira");
        pedidoBoleto.dsEspecieDocumento = rs.getString("dsEspecieDocumento");
        pedidoBoleto.dsObsCedente = rs.getString("dsObsCedente");
        pedidoBoleto.flAceite = rs.getString("flAceite");
        pedidoBoleto.dsEspecie = rs.getString("dsEspecie");
        pedidoBoleto.dtProcessamento = rs.getDate("dtProcessamento");
        pedidoBoleto.nuCarimbo = rs.getInt("nuCarimbo");
        pedidoBoleto.flTipoAlteracao = rs.getString("flTipoAlteracao");
        pedidoBoleto.cdUsuario = rs.getString("cdUsuario");
        return pedidoBoleto;
    }
    
    //@Override
    public void addSelectColumns(BaseDomain domainFilter, StringBuffer sql) throws java.sql.SQLException {
        sql.append(" rowkey,");
        sql.append(" CDEMPRESA,");
        sql.append(" CDREPRESENTANTE,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" NUSEQUENCIABOLETOPEDIDO,");
        sql.append(" CDPAGAMENTOPEDIDO,");
        sql.append(" CDBOLETOCONFIG,");
        sql.append(" CDBARRAS,");
        sql.append(" DSLINHASDIGITAVEL,");
        sql.append(" DTVENCIMENTO,");
        sql.append(" NUAGENCIACODIGOCEDENTE,");
        sql.append(" DTDOCUMENTO,");
        sql.append(" NUDOCUMENTO,");
        sql.append(" VLBOLETO,");
        sql.append(" NUNOSSONUMERO,");
        sql.append(" DSLOCALPAGAMENTO,");
        sql.append(" NUCARTEIRA,");
        sql.append(" DSESPECIEDOCUMENTO,");
        sql.append(" DSOBSCEDENTE,");
        sql.append(" FLACEITE,");
        sql.append(" DSESPECIE,");
        sql.append(" DTPROCESSAMENTO,");
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }
    
    //@Override
    protected void addInsertColumns(StringBuffer sql) throws java.sql.SQLException { 
    	 sql.append(" CDEMPRESA,");
         sql.append(" CDREPRESENTANTE,");
         sql.append(" NUPEDIDO,");
         sql.append(" FLORIGEMPEDIDO,");
         sql.append(" NUSEQUENCIABOLETOPEDIDO,");
         sql.append(" CDPAGAMENTOPEDIDO,");
         sql.append(" CDBOLETOCONFIG,");
         sql.append(" CDBARRAS,");
         sql.append(" DSLINHASDIGITAVEL,");
         sql.append(" DTVENCIMENTO,");
         sql.append(" NUAGENCIACODIGOCEDENTE,");
         sql.append(" DTDOCUMENTO,");
         sql.append(" NUDOCUMENTO,");
         sql.append(" VLBOLETO,");
         sql.append(" DSLOCALPAGAMENTO,");
         sql.append(" NUCARTEIRA,");
         sql.append(" DSESPECIEDOCUMENTO,");
         sql.append(" DSOBSCEDENTE,");
         sql.append(" FLACEITE,");
         sql.append(" NUCARIMBO,");
         sql.append(" FLTIPOALTERACAO,");
         sql.append(" CDUSUARIO,");
         sql.append(" DSESPECIE,");
         sql.append(" DTPROCESSAMENTO,");
         sql.append(" NUNOSSONUMERO" );
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { 
    	 PedidoBoleto pedidoBoleto = (PedidoBoleto) domain;
         sql.append(Sql.getValue(pedidoBoleto.cdEmpresa)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.cdRepresentante)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.nuPedido)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.flOrigemPedido)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.nuSequenciaBoletoPedido)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.cdPagamentoPedido)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.cdBoletoConfig)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.cdBarras)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.dsLinhasDigitavel)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.dtVencimento)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.nuAgenciaCodigoCedente)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.dtDocumento)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.nuDocumento)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.vlBoleto)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.dsLocalPagamento)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.nuCarteira)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.dsEspecieDocumento)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.dsObsCedente)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.flAceite)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.nuCarimbo)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.flTipoAlteracao)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.cdUsuario)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.dsEspecie)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.dtProcessamento)).append(",");
         sql.append(Sql.getValue(pedidoBoleto.nuNossoNumero));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException { 
    	  PedidoBoleto pedidoBoleto = (PedidoBoleto) domain;
          sql.append(" CDBOLETOCONFIG = ").append(Sql.getValue(pedidoBoleto.cdBoletoConfig)).append(",");
          sql.append(" CDBARRAS = ").append(Sql.getValue(pedidoBoleto.cdBarras)).append(",");
          sql.append(" DSLINHASDIGITAVEL = ").append(Sql.getValue(pedidoBoleto.dsLinhasDigitavel)).append(",");
          sql.append(" DTVENCIMENTO = ").append(Sql.getValue(pedidoBoleto.dtVencimento)).append(",");
          sql.append(" NUAGENCIACODIGOCEDENTE = ").append(Sql.getValue(pedidoBoleto.nuAgenciaCodigoCedente)).append(",");
          sql.append(" DTDOCUMENTO = ").append(Sql.getValue(pedidoBoleto.dtDocumento)).append(",");
          sql.append(" NUDOCUMENTO = ").append(Sql.getValue(pedidoBoleto.nuDocumento)).append(",");
          sql.append(" VLBOLETO = ").append(Sql.getValue(pedidoBoleto.vlBoleto)).append(",");
          sql.append(" DSLOCALPAGAMENTO = ").append(Sql.getValue(pedidoBoleto.dsLocalPagamento)).append(",");
          sql.append(" NUCARTEIRA = ").append(Sql.getValue(pedidoBoleto.nuCarteira)).append(",");
          sql.append(" DSESPECIEDOCUMENTO = ").append(Sql.getValue(pedidoBoleto.dsEspecieDocumento)).append(",");
          sql.append(" DSOBSCEDENTE = ").append(Sql.getValue(pedidoBoleto.dsObsCedente)).append(",");
          sql.append(" FLACEITE = ").append(Sql.getValue(pedidoBoleto.flAceite)).append(",");
          sql.append(" NUCARIMBO = ").append(Sql.getValue(pedidoBoleto.nuCarimbo)).append(",");
          sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(pedidoBoleto.flTipoAlteracao)).append(",");
          sql.append(" CDUSUARIO = ").append(Sql.getValue(pedidoBoleto.cdUsuario)).append(",");
          sql.append(" DSESPECIE = ").append(Sql.getValue(pedidoBoleto.dsEspecie)).append(",");
          sql.append(" DTPROCESSAMENTO = ").append(Sql.getValue(pedidoBoleto.dtProcessamento)).append(",");
          sql.append(" NUNOSSONUMERO = ").append(Sql.getValue(pedidoBoleto.nuNossoNumero)).append(",");
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PedidoBoleto pedidoBoleto = (PedidoBoleto) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pedidoBoleto.cdEmpresa);
		if (ValueUtil.isEmpty(pedidoBoleto.cdRepresentante) && ValueUtil.isNotEmpty(pedidoBoleto.cdRepresentanteSupList)) {
			sqlWhereClause.addAndConditionIn("CDREPRESENTANTE", pedidoBoleto.cdRepresentanteSupList);
		} else {
			sqlWhereClause.addAndConditionEquals("CDREPRESENTANTE", pedidoBoleto.cdRepresentante);
		}
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", pedidoBoleto.flOrigemPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", pedidoBoleto.nuPedido);
		sqlWhereClause.addAndCondition("NUSEQUENCIABOLETOPEDIDO = ", pedidoBoleto.nuSequenciaBoletoPedido);
		sqlWhereClause.addAndCondition("CDPAGAMENTOPEDIDO = ", pedidoBoleto.cdPagamentoPedido);
		sqlWhereClause.addAndCondition("CDBOLETOCONFIG = ", pedidoBoleto.cdBoletoConfig);
		sqlWhereClause.addAndCondition("NUTITULO = ", pedidoBoleto.nuTitulo);
		sqlWhereClause.addAndCondition("NUSUBDOC = ", pedidoBoleto.nuSubDoc);
		sql.append(sqlWhereClause.getSql());
    }
    
    public PedidoBoleto findLastBoleto(PedidoBoleto pedidoBoletoFilter) throws SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" select MAX(NUDOCUMENTO) as nuDocumento from ");
    	sql.append(tableName);
    	addWhereByExample(pedidoBoletoFilter, sql);
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
    		if (rs.next()) {
    			String nuDocumento = rs.getString("nuDocumento");
    			if (ValueUtil.isNotEmpty(nuDocumento)) {
    				PedidoBoleto pedidoBoleto = new PedidoBoleto();
    				pedidoBoleto.nuDocumento = new BigDecimal(nuDocumento);
    				return pedidoBoleto;
    			}
    		}
    	} catch (InvalidNumberException e) {
    		ExceptionUtil.handle(e);
		}
    	return null;
    }

    public List<PedidoBoleto> buscaBoletosPor(String cdEmpresa, String cdRepresentante, Date dtDocumento) throws java.sql.SQLException {
    	StringBuffer sql = getSqlBuffer();
    	sql.append(" SELECT cl.cdcliente, nmRazaoSocial, dtvencimento, nudocumento, vlboleto FROM ");
    	sql.append(tableName).append(" pb ");
    	sql.append(" INNER JOIN tblvppedido pe ON (pe.cdempresa = pb.cdempresa AND pe.cdrepresentante = pb.cdrepresentante AND pe.nupedido = pb.nupedido)");
    	sql.append(" INNER JOIN tblvpcliente cl ON (pe.cdempresa = cl.cdempresa AND pe.cdrepresentante = cl.cdrepresentante AND pe.cdcliente = cl.cdcliente)");
    	sql.append(" WHERE pb.cdempresa = ").append(Sql.getValue(cdEmpresa));
    	sql.append(" AND pb.cdrepresentante = ").append(Sql.getValue(cdRepresentante));
    	sql.append(" AND pb.dtdocumento = ").append(Sql.getValue(dtDocumento));
    	List<PedidoBoleto> list = new ArrayList<>();
    	try (Statement st = getCurrentDriver().getStatement();
    			ResultSet rs = st.executeQuery(sql.toString())) {
			while (rs.next()) {
				PedidoBoleto boleto = new PedidoBoleto();
				boleto.cdCliente = rs.getString("cdcliente");
				boleto.nmCliente = rs.getString("nmRazaoSocial");
				boleto.dtVencimento = rs.getDate("dtvencimento");
				boleto.nuDocumento = rs.getBigDecimal("nudocumento");
				boleto.vlBoleto = rs.getDouble("vlboleto");
				list.add(boleto);
			}
			return list;
		}
	}

	public PedidoBoleto buscaBoletoPorTitulo(PedidoBoleto pedidoBoleto) throws SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" select ");
		addSelectColumns(pedidoBoleto, sql);
		sql.append(" from ");
		sql.append(tableName);
		sql.append(" tb ");
		addWhereByExample(pedidoBoleto, sql);
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			if (rs.next()) {
				return (PedidoBoleto) populate(pedidoBoleto, rs);
			}
		}
		return null;
	}

}