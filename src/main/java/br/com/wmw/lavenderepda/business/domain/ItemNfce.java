package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.dto.ItemNfceDTO;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.util.Date;

public class ItemNfce extends BaseDomain {

	public static String TABLE_NAME = "TBLVPITEMNFCE";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String flOrigemPedido;
    public String cdProduto;
    public int nuSeqProduto;
    public double qtItemFisico;
    public String cdUnidade;
    public double vlUnitario;
    public double vlTotalItem;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    //Não persistentes
    private Produto produto;
    
    public ItemNfce() {}
    
    public ItemNfce(ItemNfceDTO itemNfceDTO) {
    	try {
    		FieldMapper.copy(itemNfceDTO, this);
    	} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemNfce) {
            ItemNfce itemnfce = (ItemNfce) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, itemnfce.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, itemnfce.cdRepresentante) && 
                ValueUtil.valueEquals(nuPedido, itemnfce.nuPedido) && 
                ValueUtil.valueEquals(flOrigemPedido, itemnfce.flOrigemPedido) && 
                ValueUtil.valueEquals(cdProduto, itemnfce.cdProduto) && 
                ValueUtil.valueEquals(nuSeqProduto, itemnfce.nuSeqProduto);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(flOrigemPedido);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(nuSeqProduto);
        return primaryKey.toString();
    }

	public Produto getProduto() throws SQLException {
		if ((!ValueUtil.isEmpty(cdProduto)) && ((produto == null) || (!cdProduto.equals(produto.cdProduto)))) {
    		produto = ProdutoService.getInstance().getProduto(cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdProduto);
    	}
    	return produto;
	}
	
	public String getDsProduto() throws SQLException {
    	if ((!ValueUtil.isEmpty(cdProduto)) && ((produto == null) || (!cdProduto.equals(produto.cdProduto)))) {
    		produto = ProdutoService.getInstance().getProduto(cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdProduto);
    	}
    	return ProdutoService.getInstance().getDsProduto(this.produto);
    }
	
}