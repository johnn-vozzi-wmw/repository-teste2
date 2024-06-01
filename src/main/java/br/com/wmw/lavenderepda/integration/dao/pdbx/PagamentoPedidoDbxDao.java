package br.com.wmw.lavenderepda.integration.dao.pdbx;

import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.Sql;
import br.com.wmw.framework.integration.dao.SqlWhereClause;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import totalcross.sql.ResultSet;
import totalcross.sql.Statement;
import totalcross.util.Date;

public class PagamentoPedidoDbxDao extends LavendereCrudDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new PagamentoPedido();
	}

    private static PagamentoPedidoDbxDao instance;
    private static PagamentoPedidoDbxDao instanceErp;

    public PagamentoPedidoDbxDao() {
        super(PagamentoPedido.TABLE_NAME);
    }
    
    public PagamentoPedidoDbxDao(String tableName) {
    	super(tableName);
    }
    
    public static PagamentoPedidoDbxDao getInstance() {
        if (instance == null) {
            instance = new PagamentoPedidoDbxDao(PagamentoPedido.TABLE_NAME);
        }
        return instance;
    }

    public static PagamentoPedidoDbxDao getInstanceErp() {
    	if (instanceErp == null) {
    		instanceErp = new PagamentoPedidoDbxDao(PagamentoPedido.TABLE_NAME_ERP);
    	}
    	return instanceErp;
    }
    
    //@Override
    protected BaseDomain populate(BaseDomain domainFilter, ResultSet rs) throws java.sql.SQLException {
        PagamentoPedido pagamentoPedido = new PagamentoPedido();
        pagamentoPedido.rowKey = rs.getString("rowkey");
        pagamentoPedido.cdEmpresa = rs.getString("cdEmpresa");
        pagamentoPedido.cdRepresentante = rs.getString("cdRepresentante");
        pagamentoPedido.cdPagamentoPedido = rs.getString("cdPagamentoPedido");
        pagamentoPedido.nuPedido = rs.getString("nuPedido");
        pagamentoPedido.flOrigemPedido = rs.getString("flOrigemPedido");
        pagamentoPedido.cdTipoPagamento = rs.getString("cdTipoPagamento");
        pagamentoPedido.cdCondicaoPagamento = rs.getString("cdCondicaoPagamento");
        pagamentoPedido.vlPagamentoPedido = ValueUtil.round(rs.getDouble("vlPagamentoPedido"));
      	pagamentoPedido.vlDesconto = ValueUtil.round(rs.getDouble("vlDesconto"));
        if (LavenderePdaConfig.isUsaInformacoesAdicionaisPagamentoCheque() || LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
        	pagamentoPedido.nuBanco = rs.getString("nuBanco");
        	pagamentoPedido.nuComplemento = rs.getString("nuComplemento");
        	pagamentoPedido.nuAgencia  = rs.getString("nuAgencia");
        	pagamentoPedido.nuConta = rs.getString("nuConta");
        	pagamentoPedido.nuCheque  = rs.getString("nuCheque");
        	pagamentoPedido.flChequeTerceiro = rs.getString("flChequeTerceiro");
        	pagamentoPedido.dsEmitente = rs.getString("dsEmitente");
        	pagamentoPedido.dsReferenteCheque = rs.getString("dsReferenteCheque");
        	pagamentoPedido.dsBanco = rs.getString("dsBanco");
        	pagamentoPedido.dtCheque = rs.getDate("dtCheque");
        }
        if (LavenderePdaConfig.nuDiasMaximoVencimentoToleranciaPagamento > 0 || LavenderePdaConfig.permiteAlterarVencimentoConformeCondPagto || LavenderePdaConfig.permiteAlterarVencimentoConformeCliente) {
        	pagamentoPedido.dtVencimento = rs.getDate("dtVencimento");
        }
        if (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido) {
        	pagamentoPedido.vlPagamentoBruto = rs.getDouble("vlPagamentoBruto");
        	pagamentoPedido.vlIndicePagamento = rs.getDouble("vlIndicePagamento");
        }
        pagamentoPedido.nuCarimbo = rs.getInt("nuCarimbo");
        pagamentoPedido.flTipoAlteracao = rs.getString("flTipoAlteracao");
        pagamentoPedido.cdUsuario = rs.getString("cdUsuario");
        if (LavenderePdaConfig.usaVencimentosAdicionaisBoleto) {
        	pagamentoPedido.cdVencimentoAdicBoleto = rs.getString("cdVencimentoAdicBoleto");
        }
        return pagamentoPedido;
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
        sql.append(" CDPAGAMENTOPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" CDTIPOPAGAMENTO,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" VLPAGAMENTOPEDIDO,");
        sql.append(" VLDESCONTO,");
        if (LavenderePdaConfig.isUsaInformacoesAdicionaisPagamentoCheque() || LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
        	sql.append(" NUBANCO,");
        	sql.append(" NUCOMPLEMENTO,");
        	sql.append(" NUAGENCIA,");
        	sql.append(" NUCONTA,");
        	sql.append(" NUCHEQUE,");
        	sql.append(" FLCHEQUETERCEIRO,");
        	sql.append(" DSEMITENTE,");
        	sql.append(" DSREFERENTECHEQUE,");
        	sql.append(" DSBANCO,");
        	sql.append(" DTCHEQUE,");
        }
        if (LavenderePdaConfig.nuDiasMaximoVencimentoToleranciaPagamento > 0 || LavenderePdaConfig.permiteAlterarVencimentoConformeCondPagto || LavenderePdaConfig.permiteAlterarVencimentoConformeCliente) {
        	sql.append(" DTVENCIMENTO,");
        }
        if (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido) {
        	sql.append(" VLPAGAMENTOBRUTO,");
        	sql.append(" VLINDICEPAGAMENTO,");
        }
        if (LavenderePdaConfig.usaVencimentosAdicionaisBoleto) {
        	sql.append(" CDVENCIMENTOADICBOLETO,");
        }
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
        sql.append(" CDREPRESENTANTE,");
        sql.append(" CDPAGAMENTOPEDIDO,");
        sql.append(" NUPEDIDO,");
        sql.append(" FLORIGEMPEDIDO,");
        sql.append(" CDTIPOPAGAMENTO,");
        sql.append(" CDCONDICAOPAGAMENTO,");
        sql.append(" VLPAGAMENTOPEDIDO,");
       	sql.append(" VLDESCONTO,");
        if (LavenderePdaConfig.isUsaInformacoesAdicionaisPagamentoCheque()) {
        	sql.append(" NUBANCO,");
        	sql.append(" NUCOMPLEMENTO,");
        	sql.append(" NUAGENCIA,");
        	sql.append(" NUCONTA,");
        	sql.append(" NUCHEQUE,");
        	sql.append(" FLCHEQUETERCEIRO,");
        	sql.append(" DSEMITENTE,");
        	sql.append(" DSREFERENTECHEQUE,");
        	sql.append(" DSBANCO,");
        	sql.append(" DTCHEQUE,");
        }
        if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
        	sql.append(" NUBANCO,");
        	sql.append(" NUAGENCIA,");
        	sql.append(" NUCONTA,");
        }
        if (LavenderePdaConfig.nuDiasMaximoVencimentoToleranciaPagamento > 0 || LavenderePdaConfig.permiteAlterarVencimentoConformeCondPagto || LavenderePdaConfig.permiteAlterarVencimentoConformeCliente) {
        	sql.append(" DTVENCIMENTO,");
        }
//        if (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido) {
        	sql.append(" VLPAGAMENTOBRUTO,");
        	sql.append(" VLINDICEPAGAMENTO,");
//        }
        if (LavenderePdaConfig.usaVencimentosAdicionaisBoleto) {
        	sql.append(" CDVENCIMENTOADICBOLETO,");
        }
        sql.append(" NUCARIMBO,");
        sql.append(" FLTIPOALTERACAO,");
        sql.append(" CDUSUARIO");
    }

    //@Override
    protected void addInsertValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PagamentoPedido pagamentoPedido = (PagamentoPedido) domain;
        sql.append(Sql.getValue(pagamentoPedido.cdEmpresa)).append(",");
        sql.append(Sql.getValue(pagamentoPedido.cdRepresentante)).append(",");
        sql.append(Sql.getValue(pagamentoPedido.cdPagamentoPedido)).append(",");
        sql.append(Sql.getValue(pagamentoPedido.nuPedido)).append(",");
        sql.append(Sql.getValue(pagamentoPedido.flOrigemPedido)).append(",");
        sql.append(Sql.getValue(pagamentoPedido.cdTipoPagamento)).append(",");
        sql.append(Sql.getValue(pagamentoPedido.cdCondicaoPagamento)).append(",");
        sql.append(Sql.getValue(pagamentoPedido.vlPagamentoPedido)).append(",");
       	sql.append(Sql.getValue(pagamentoPedido.vlDesconto)).append(",");
        if (LavenderePdaConfig.isUsaInformacoesAdicionaisPagamentoCheque()) {
        	sql.append(Sql.getValue(pagamentoPedido.nuBanco)).append(",");
        	sql.append(Sql.getValue(pagamentoPedido.nuComplemento)).append(",");
        	sql.append(Sql.getValue(pagamentoPedido.nuAgencia)).append(",");
        	sql.append(Sql.getValue(pagamentoPedido.nuConta)).append(",");
        	sql.append(Sql.getValue(pagamentoPedido.nuCheque)).append(",");
        	sql.append(Sql.getValue(pagamentoPedido.flChequeTerceiro)).append(",");
        	sql.append(Sql.getValue(pagamentoPedido.dsEmitente)).append(",");
        	sql.append(Sql.getValue(pagamentoPedido.dsReferenteCheque)).append(",");
        	sql.append(Sql.getValue(pagamentoPedido.dsBanco)).append(",");
        	sql.append(Sql.getValue(pagamentoPedido.dtCheque)).append(",");
        }
        if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
        	sql.append(Sql.getValue(pagamentoPedido.nuBanco)).append(",");
        	sql.append(Sql.getValue(pagamentoPedido.nuAgencia)).append(",");
        	sql.append(Sql.getValue(pagamentoPedido.nuConta)).append(",");
        }
        if (LavenderePdaConfig.nuDiasMaximoVencimentoToleranciaPagamento > 0 || LavenderePdaConfig.permiteAlterarVencimentoConformeCondPagto || LavenderePdaConfig.permiteAlterarVencimentoConformeCliente) {
        	sql.append(Sql.getValue(pagamentoPedido.dtVencimento)).append(",");
        }
//        if (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido) {
        	sql.append(Sql.getValue(pagamentoPedido.vlPagamentoBruto)).append(",");
        	sql.append(Sql.getValue(pagamentoPedido.vlIndicePagamento)).append(",");
//        }
        if (LavenderePdaConfig.usaVencimentosAdicionaisBoleto) {
        	sql.append(Sql.getValue(pagamentoPedido.cdVencimentoAdicBoleto)).append(",");
        }
        sql.append(Sql.getValue(pagamentoPedido.nuCarimbo)).append(",");
        sql.append(Sql.getValue(pagamentoPedido.flTipoAlteracao)).append(",");
        sql.append(Sql.getValue(pagamentoPedido.cdUsuario));
    }

    //@Override
    protected void addUpdateValues(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PagamentoPedido pagamentoPedido = (PagamentoPedido) domain;
        sql.append(" CDTIPOPAGAMENTO = ").append(Sql.getValue(pagamentoPedido.cdTipoPagamento)).append(",");
        sql.append(" CDCONDICAOPAGAMENTO = ").append(Sql.getValue(pagamentoPedido.cdCondicaoPagamento)).append(",");
        sql.append(" VLPAGAMENTOPEDIDO = ").append(Sql.getValue(pagamentoPedido.vlPagamentoPedido)).append(",");
       	sql.append(" VLDESCONTO = ").append(Sql.getValue(pagamentoPedido.vlDesconto)).append(",");
        if (LavenderePdaConfig.isUsaInformacoesAdicionaisPagamentoCheque()) {
        	sql.append(" NUBANCO = ").append(Sql.getValue(pagamentoPedido.nuBanco)).append(",");
        	sql.append(" NUCOMPLEMENTO = ").append(Sql.getValue(pagamentoPedido.nuComplemento)).append(",");
        	sql.append(" NUAGENCIA = ").append(Sql.getValue(pagamentoPedido.nuAgencia)).append(",");
        	sql.append(" NUCONTA = ").append(Sql.getValue(pagamentoPedido.nuConta)).append(",");
        	sql.append(" NUCHEQUE = ").append(Sql.getValue(pagamentoPedido.nuCheque)).append(",");
        	sql.append(" FLCHEQUETERCEIRO = ").append(Sql.getValue(pagamentoPedido.flChequeTerceiro)).append(",");
        	sql.append(" DSEMITENTE = ").append(Sql.getValue(pagamentoPedido.dsEmitente)).append(",");
        	sql.append(" DSREFERENTECHEQUE = ").append(Sql.getValue(pagamentoPedido.dsReferenteCheque)).append(",");
        	sql.append(" DSBANCO = ").append(Sql.getValue(pagamentoPedido.dsBanco)).append(",");
        	sql.append(" DTCHEQUE = ").append(Sql.getValue(pagamentoPedido.dtCheque)).append(",");
        }
        if (LavenderePdaConfig.isUsaIndicacaoDadosBancariosClienteNoPedido()) {
        	sql.append(" NUBANCO = ").append(Sql.getValue(pagamentoPedido.nuBanco)).append(",");
        	sql.append(" NUAGENCIA = ").append(Sql.getValue(pagamentoPedido.nuAgencia)).append(",");
        	sql.append(" NUCONTA = ").append(Sql.getValue(pagamentoPedido.nuConta)).append(",");
        }
        if (LavenderePdaConfig.nuDiasMaximoVencimentoToleranciaPagamento > 0 || LavenderePdaConfig.permiteAlterarVencimentoConformeCondPagto || LavenderePdaConfig.permiteAlterarVencimentoConformeCliente) {
        	sql.append(" DTVENCIMENTO = ").append(Sql.getValue(pagamentoPedido.dtVencimento)).append(",");
        }
        if (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido) {
        	sql.append(" VLPAGAMENTOBRUTO = ").append(Sql.getValue(pagamentoPedido.vlPagamentoBruto)).append(",");
        	sql.append(" VLINDICEPAGAMENTO = ").append(Sql.getValue(pagamentoPedido.vlIndicePagamento)).append(",");
        }
        if (LavenderePdaConfig.usaVencimentosAdicionaisBoleto) {
        	sql.append(" CDVENCIMENTOADICBOLETO = ").append(Sql.getValue(pagamentoPedido.cdVencimentoAdicBoleto)).append(",");
        }
        sql.append(" NUCARIMBO = ").append(Sql.getValue(pagamentoPedido.nuCarimbo)).append(",");
        sql.append(" FLTIPOALTERACAO = ").append(Sql.getValue(pagamentoPedido.flTipoAlteracao)).append(",");
        sql.append(" CDUSUARIO = ").append(Sql.getValue(pagamentoPedido.cdUsuario));
    }
    
    //@Override
    protected void addWhereByExample(BaseDomain domain, StringBuffer sql) throws java.sql.SQLException {
        PagamentoPedido pagamentoPedido = (PagamentoPedido) domain;
		SqlWhereClause sqlWhereClause = new SqlWhereClause();
		sqlWhereClause.addAndCondition("CDEMPRESA = ", pagamentoPedido.cdEmpresa);
		sqlWhereClause.addAndCondition("CDREPRESENTANTE = ", pagamentoPedido.cdRepresentante);
		sqlWhereClause.addAndCondition("CDPAGAMENTOPEDIDO = ", pagamentoPedido.cdPagamentoPedido);
		sqlWhereClause.addAndCondition("NUPEDIDO = ", pagamentoPedido.nuPedido);
		sqlWhereClause.addAndCondition("FLORIGEMPEDIDO = ", pagamentoPedido.flOrigemPedido);
		sqlWhereClause.addAndCondition("CDTIPOPAGAMENTO = ", pagamentoPedido.cdTipoPagamento);
		//--
		sql.append(sqlWhereClause.getSql());
    }
    
    @Override
    public BaseDomain findByRowKey(String rowKey) throws java.sql.SQLException {
		StringBuffer strBuffer = getSqlBuffer();
		strBuffer.append(";").append(OrigemPedido.FLORIGEMPEDIDO_PDA).append(";");
		if (rowKey.indexOf(strBuffer.toString()) == -1) {
			return getInstanceErp().findByRowKeyErp(rowKey);
		}
		return super.findByRowKey(rowKey);
	}
    
    private BaseDomain findByRowKeyErp(String rowKey) throws java.sql.SQLException {
		return super.findByRowKey(rowKey);
	}
 
	@Override
	protected boolean isNotSaveTabelasEnvio() {
		return true;
	}
	
	public double somaDinheiroPedidoPorData(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws java.sql.SQLException {
		StringBuffer sql = getSqlTotalPagamento(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal, true);
		sql.append(" AND (tp.flCheque is null OR tp.flCheque != 'S') ");
		sql.append(" AND (tp.flBoleto is null OR tp.flBoleto != 'S') ");
		sql.append(" AND (TPE.FLBONIFICACAO IS NULL OR TPE.FLBONIFICACAO != 'S') ");
		return getDouble(sql.toString());
	}

	public double somaChequePedidoPorData(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws java.sql.SQLException {
		StringBuffer sql = getSqlTotalPagamento(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal, true);
		sql.append(" AND (TP.FLCHEQUE = 'S') ");
		sql.append(" AND (TP.FLBOLETO IS NULL OR TP.FLBOLETO != 'S') ");
		sql.append(" AND (TPE.FLBONIFICACAO IS NULL OR TPE.FLBONIFICACAO != 'S') ");
		return getDouble(sql.toString());
	}
	
	public double somaBoletoPedidoPorData(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws java.sql.SQLException {
		StringBuffer sql = getSqlTotalPagamento(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal, true);
		sql.append(" AND (TP.FLCHEQUE IS NULL OR TP.FLCHEQUE != 'S') ");
		sql.append(" AND TP.FLBOLETO = 'S' ");
		sql.append(" AND (TPE.FLBONIFICACAO IS NULL OR TPE.FLBONIFICACAO != 'S') ");
		return getDouble(sql.toString());
	}
	
	public double somaOutrosPedidoPorData(String cdEmpresa, String cdRepresentante, Date dtEmissaoInicial, Date dtEmissaoFinal) throws java.sql.SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT SUM(VLPAGAMENTOPEDIDO) AS TOTAL  ");
		sql.append(" FROM ").append(tableName).append(" AS PG ");
		sql.append(" INNER JOIN TBLVPPEDIDO PE ON (PE.CDEMPRESA = PG.CDEMPRESA AND PE.CDREPRESENTANTE = PG.CDREPRESENTANTE AND PE.NUPEDIDO = PG.NUPEDIDO) ");
		sql.append(" INNER JOIN TBLVPTIPOPEDIDO TPE ON (TPE.CDEMPRESA = PE.CDEMPRESA AND TPE.CDREPRESENTANTE = PE.CDREPRESENTANTE AND TPE.CDTIPOPEDIDO = PE.CDTIPOPEDIDO) ");
		sql.append(" WHERE PG.CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
		sql.append(" AND PG.CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
		sql.append(" AND PE.DTEMISSAO >= ").append(Sql.getValue(dtEmissaoInicial));
		sql.append(" AND PE.DTEMISSAO <= ").append(Sql.getValue(dtEmissaoFinal));
		sql.append(" AND PE.CDSTATUSPEDIDO != ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoAberto));
		sql.append(" AND PE.CDSTATUSPEDIDO != ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoCancelado));	
		sql.append(" AND ( TPE.FLBONIFICACAO IS NULL OR TPE.FLBONIFICACAO != 'S' ) ");
		sql.append(" AND NOT EXISTS (SELECT CDTIPOPAGAMENTO FROM TBLVPTIPOPAGAMENTO TP "); 
		sql.append("                  WHERE TP.CDEMPRESA = PG.CDEMPRESA ");
		sql.append("                    AND TP.CDREPRESENTANTE = PG.CDREPRESENTANTE ");
		sql.append(" 				    AND TP.CDTIPOPAGAMENTO = PG.CDTIPOPAGAMENTO) ");
		return getDouble(sql.toString());
	}

	public double somaTotalVendasPorData(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws java.sql.SQLException {
		StringBuffer sql = getSqlTotalPagamento(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal, false);
		sql.append(" AND (TPE.FLBONIFICACAO IS NULL OR TPE.FLBONIFICACAO != 'S') ");
		return getDouble(sql.toString());
	}

	public double somaTotalPedidosPorData(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws java.sql.SQLException {
		StringBuffer sql = getSqlTotalPagamento(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal, false);
		return getDouble(sql.toString());
	}
	
	public double somaTotalReceitasPorData(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws java.sql.SQLException {
		double somaTotalVendasPorData = somaTotalVendasPorData(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal);
		double somaTotalPagamentosPorData = PagamentoPdbxDao.getInstance().somaTotalPagamentosPorData(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal);
		return somaTotalVendasPorData + somaTotalPagamentosPorData;
	}
	
	public double somaTotalBonificacaoPorData(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws java.sql.SQLException {
		StringBuffer sql = getSqlTotalPagamento(cdEmpresa, cdRepresentante, dtFechamentoInicial, dtFechamentoFinal, false);
		sql.append(" AND TPE.FLBONIFICACAO = 'S' ");
		return getDouble(sql.toString());
	}

	private StringBuffer getSqlTotalPagamento(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal, boolean addJoinTipoPagamento) {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT SUM(VLPAGAMENTOPEDIDO) AS TOTAL  ");
		sql.append(" FROM ").append(tableName).append(" AS PG ");
		if (addJoinTipoPagamento) {
			sql.append(" INNER JOIN TBLVPTIPOPAGAMENTO TP ON (TP.CDEMPRESA = PG.CDEMPRESA AND TP.CDREPRESENTANTE = PG.CDREPRESENTANTE AND TP.CDTIPOPAGAMENTO = PG.CDTIPOPAGAMENTO) ");
		}
		sql.append(" INNER JOIN TBLVPPEDIDO PE ON (PE.CDEMPRESA = PG.CDEMPRESA AND PE.CDREPRESENTANTE = PG.CDREPRESENTANTE AND PE.NUPEDIDO = PG.NUPEDIDO) ");
		sql.append(" INNER JOIN TBLVPTIPOPEDIDO TPE ON (TPE.CDEMPRESA = PE.CDEMPRESA AND TPE.CDREPRESENTANTE = PE.CDREPRESENTANTE AND TPE.CDTIPOPEDIDO = PE.CDTIPOPEDIDO) ");
		sql.append(" WHERE PG.CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
		sql.append(" AND PG.CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
		sql.append(" AND PE.DTFECHAMENTO >= ").append(Sql.getValue(dtFechamentoInicial));
		sql.append(" AND PE.DTFECHAMENTO <= ").append(Sql.getValue(dtFechamentoFinal));
		sql.append(" AND PE.CDSTATUSPEDIDO != ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoAberto));
		sql.append(" AND PE.CDSTATUSPEDIDO != ").append(Sql.getValue(LavenderePdaConfig.cdStatusPedidoCancelado));
		return sql;
	}
	
	public List<PagamentoPedido> buscaDadosChequePor(String cdEmpresa, String cdRepresentante, Date dtFechamentoInicial, Date dtFechamentoFinal) throws java.sql.SQLException {
		StringBuffer sql = getSqlBuffer();
		sql.append(" SELECT cl.cdcliente, nmRazaoSocial, nunfe, nucheque, vlpagamentopedido, dsBanco, dtCheque ");
		sql.append(" FROM tblvppagamentopedido pp ");
		sql.append(" INNER JOIN TBLVPTIPOPAGAMENTO tp on (tp.cdempresa = pp.cdempresa AND tp.cdrepresentante = pp.cdrepresentante AND tp.cdTipoPagamento = pp.cdTipoPagamento AND tp.flcheque = 'S') ");
		sql.append(" INNER JOIN tblvppedido pe ON (pe.cdempresa = pp.cdempresa AND pe.cdrepresentante = pp.cdrepresentante AND pe.nupedido = pp.nupedido) ");
		sql.append(" INNER JOIN tblvpcliente cl ON (cl.cdempresa = pp.cdempresa AND cl.cdrepresentante = pp.cdrepresentante AND cl.cdcliente = pe.cdcliente) ");
		sql.append(" LEFT JOIN tblvpNFE nf ON (nf.cdempresa = pp.cdempresa AND nf.cdrepresentante = pp.cdrepresentante AND nf.nupedido = pp.nupedido) ");
		sql.append(" WHERE PP.CDEMPRESA = ").append(Sql.getValue(cdEmpresa));
		sql.append(" AND PP.CDREPRESENTANTE = ").append(Sql.getValue(cdRepresentante));
		sql.append(" AND PE.DTFECHAMENTO >= ").append(Sql.getValue(dtFechamentoInicial));
		sql.append(" AND PE.DTFECHAMENTO <= ").append(Sql.getValue(dtFechamentoFinal));
		try (Statement st = getCurrentDriver().getStatement();
				ResultSet rs = st.executeQuery(sql.toString())) {
			List<PagamentoPedido> lista = new ArrayList<>();
			while (rs.next()) {
				PagamentoPedido pagamentoPedido = new PagamentoPedido();
				pagamentoPedido.cdcliente = rs.getString("cdcliente");
				pagamentoPedido.nmRazaoSocial = rs.getString("nmRazaoSocial");
				pagamentoPedido.nuNfe = rs.getString("nunfe");
				pagamentoPedido.nuCheque = rs.getString("nucheque");
				pagamentoPedido.vlPagamentoPedido = rs.getDouble("vlpagamentopedido");
				pagamentoPedido.dsBanco = rs.getString("dsBanco");
				pagamentoPedido.dtCheque = rs.getDate("dtCheque");
				lista.add(pagamentoPedido);
			}
			return lista;
		}
	}

}