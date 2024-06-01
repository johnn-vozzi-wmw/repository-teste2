package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.dto.PedidoEstoqueDto;
import br.com.wmw.lavenderepda.business.domain.dto.RemessaEstoqueDto;
import totalcross.util.Date;

public class RemessaEstoque extends BaseDomain {

	public static String TABLE_NAME = "TBLVPREMESSAESTOQUE";
	public static final String FL_FINALIZADA = "F";

    public String cdEmpresa;
    public String cdRepresentante;
    public String nuNotaRemessa;
    public String nuSerieRemessa;
    public String cdLocalEstoque;
    public Date dtRemessa;
    public String hrRemessa;
    public String vlChaveAcesso;
    public String flEstoqueLiberado;
    public Date dtAlteracao;
    public String hrAlteracao;
    public String flFinalizada;
    public String flTipoRemessa;
    
    public RemessaEstoque() {}
    
    public RemessaEstoque(RemessaEstoqueDto dto) {
    	try {
    		FieldMapper.copy(dto, this);
    	} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
    }
    public Date dtFinalizacao;
    public String hrFinalizacao;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RemessaEstoque) {
            RemessaEstoque remessaEstoque = (RemessaEstoque) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, remessaEstoque.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, remessaEstoque.cdRepresentante) && 
                ValueUtil.valueEquals(nuNotaRemessa, remessaEstoque.nuNotaRemessa) && 
                ValueUtil.valueEquals(nuSerieRemessa, remessaEstoque.nuSerieRemessa);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(nuNotaRemessa);
        primaryKey.append(";");
        primaryKey.append(nuSerieRemessa);
        return primaryKey.toString();
    }
    
    public String getDsStatus(String cdStatusInterface) {
    	if (ValueUtil.isEmpty(cdStatusInterface)) {
    		return isFinalizada() ? Messages.REMESSAESTOQUE_LABEL_FINALIZADA : getDsEstoqueLiberado();
    	}
    	return isFinalizada() && ValueUtil.valueEquals(FL_FINALIZADA, cdStatusInterface) ? Messages.REMESSAESTOQUE_LABEL_FINALIZADA : getDsEstoqueLiberado();
    }

	public String getDsEstoqueLiberado() {
		return isEstoqueLiberado() ? Messages.REMESSAESTOQUE_LABEL_LIBERADA : Messages.REMESSAESTOQUE_LABEL_BLOQUEADA;
	}
	
	public boolean isEstoqueLiberado() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flEstoqueLiberado);
	}
	
	public boolean isFinalizada() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flFinalizada);
	}
	
	public boolean isFinalizadaFiltroNaTela() {
		return ValueUtil.valueEquals(FL_FINALIZADA, flFinalizada);
	}
	
	public String getDsTipoRemessa() {
		return PedidoEstoqueDto.TIPOREMESSA_R.equals(flTipoRemessa) ? Messages.REPRESENTANTE_NOME_ENTIDADE : Messages.REMESSAESTOQUE_LABEL_EMPRESA;
	}

}