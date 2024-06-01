package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import totalcross.util.Date;

public class RemessaEstoqueDto {
	
	public String cdEmpresa;
    public String cdRepresentante;
    public String nuNotaRemessa;
    public String nuSerieRemessa;
    public String cdLocalEstoque;
    public Date dtRemessa;
    public String hrRemessa;
    public String vlChaveAcesso;
    public String flEstoqueLiberado;
    public String flTipoRemessa;
    public EstoqueDto[] estoqueList;
    public NfeEstoqueDto nfeEstoque;
    
    public RemessaEstoqueDto() {}
    
    public RemessaEstoqueDto(RemessaEstoque remessaEstoque) {
		try {
			FieldMapper.copy(remessaEstoque, this);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	public String getCdEmpresa() {
		return cdEmpresa;
	}

	public void setCdEmpresa(String cdEmpresa) {
		this.cdEmpresa = cdEmpresa;
	}

	public String getCdRepresentante() {
		return cdRepresentante;
	}

	public void setCdRepresentante(String cdRepresentante) {
		this.cdRepresentante = cdRepresentante;
	}

	public String getNuNotaRemessa() {
		return nuNotaRemessa;
	}

	public void setNuNotaRemessa(String nuNotaRemessa) {
		this.nuNotaRemessa = nuNotaRemessa;
	}

	public String getNuSerieRemessa() {
		return nuSerieRemessa;
	}

	public void setNuSerieRemessa(String nuSerieRemessa) {
		this.nuSerieRemessa = nuSerieRemessa;
	}

	public String getCdLocalEstoque() {
		return cdLocalEstoque;
	}

	public void setCdLocalEstoque(String cdLocalEstoque) {
		this.cdLocalEstoque = cdLocalEstoque;
	}

	public String getDtRemessa() {
		return dtRemessa == null ? null : DateUtil.formatDateDDMMYYYY(dtRemessa);
	}

	public void setDtRemessa(Date dtRemessa) {
		this.dtRemessa = dtRemessa;
	}

	public String getHrRemessa() {
		return hrRemessa;
	}

	public void setHrRemessa(String hrRemessa) {
		this.hrRemessa = hrRemessa;
	}

	public String getVlChaveAcesso() {
		return vlChaveAcesso;
	}

	public void setVlChaveAcesso(String vlChaveAcesso) {
		this.vlChaveAcesso = vlChaveAcesso;
	}

	public String getFlEstoqueLiberado() {
		return flEstoqueLiberado;
	}

	public void setFlEstoqueLiberado(String flEstoqueLiberado) {
		this.flEstoqueLiberado = flEstoqueLiberado;
	}

	public EstoqueDto[] getEstoqueList() {
		return estoqueList;
	}

	public void setEstoqueList(EstoqueDto[] estoqueList) {
		this.estoqueList = estoqueList;
	}

	public String getFlTipoRemessa() {
		return flTipoRemessa;
	}

	public void setFlTipoRemessa(String flTipoRemessa) {
		this.flTipoRemessa = flTipoRemessa;
	}

	public NfeEstoqueDto getNfeEstoque() {
		return nfeEstoque;
	}

	public void setNfeEstoque(NfeEstoqueDto nfeEstoque) {
		this.nfeEstoque = nfeEstoque;
	}
    
}