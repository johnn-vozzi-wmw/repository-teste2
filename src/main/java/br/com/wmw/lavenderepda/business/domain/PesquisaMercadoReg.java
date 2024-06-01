package br.com.wmw.lavenderepda.business.domain;

import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class PesquisaMercadoReg extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPPESQUISAMERCADOREG";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdPesquisaMercadoConfig;
	public String cdCliente;
	public String cdProduto;
	public String cdConcorrente;
	public Date dtEmissao;
	public Date dtInsercaoProduto;
	public String hrInsercaoProduto;
	public double vlUnitario;
	public String flControleWmw;
	public String dsMensagemControleWmw;
	public String flControleErp;
	public String dsMensagemControleErp;
	public Date dtRecebimento;
	public String hrRecebimento;
	public Date dtEnvioErp;
	public String hrEnvioErp;
	public String cdUsuarioEmissao;
	public String nuPedido;
	public String flOrigemPedido;
	public Double cdLatitude;
	public Double cdLongitude;
	public String flFinalizada;

	//não persistentes
	public List<PesquisaMercadoReg> pesquisaMercadoRegBundle;

	@Override
	public String getPrimaryKey() {
		StringBuilder pk = new StringBuilder();
		pk.append(cdEmpresa).append(";");
		pk.append(cdRepresentante).append(";");
		pk.append(cdPesquisaMercadoConfig).append(";");
		pk.append(cdCliente).append(";");
		pk.append(cdProduto).append(";");
		pk.append(cdConcorrente).append(";");
		return pk.toString();
	}

	public boolean isFinalizada() {
		return ValueUtil.getBooleanValue(flFinalizada);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PesquisaMercadoReg) {
			PesquisaMercadoReg other = (PesquisaMercadoReg) obj;
			return ValueUtil.valueEquals(cdEmpresa, other.cdEmpresa)
					&& ValueUtil.valueEquals(cdRepresentante, other.cdRepresentante)
					&& ValueUtil.valueEquals(cdPesquisaMercadoConfig, other.cdPesquisaMercadoConfig)
					&& ValueUtil.valueEquals(cdCliente, other.cdCliente)
					&& ValueUtil.valueEquals(cdProduto, other.cdProduto)
					&& ValueUtil.valueEquals(cdConcorrente, other.cdConcorrente);
		} else {
			return false;
		}
	}

}
