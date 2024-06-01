package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoErpDifDTO;
import br.com.wmw.lavenderepda.business.domain.dto.PedidoErpDifDTO;
import totalcross.util.Vector;

public class PedidoErpDif extends BaseDomain {

    public static String TABLE_NAME = "TBLVPPEDIDOERPDIF";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdStatusPedido;
    public String dsObservacaoorg;
    public String dsObservacaoerp;
    
    public Vector itemPedidoErpDifList;
    
    public PedidoErpDif() {
	}
    
    public PedidoErpDif(PedidoErpDifDTO pedidoErpDifDTO) {
    	this();
    	try {
    		FieldMapper.copy(pedidoErpDifDTO, this);
    		ItemPedidoErpDifDTO[] itemPedidoErpDifDTOList = pedidoErpDifDTO.getItemPedidoErpDifList();
    		if (itemPedidoErpDifDTOList != null && itemPedidoErpDifDTOList.length > 0) {
    			itemPedidoErpDifList = new Vector(itemPedidoErpDifDTOList.length);
    			for (int i = 0; i < itemPedidoErpDifDTOList.length; i++) {
    				itemPedidoErpDifList.addElement(new ItemPedidoErpDif(itemPedidoErpDifDTOList[i]));
    			}
    		}
    	} catch (Throwable e) {
			e.printStackTrace();
		}
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof PedidoErpDif) {
            PedidoErpDif pedidoerpdif = (PedidoErpDif) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, pedidoerpdif.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, pedidoerpdif.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemPedido, pedidoerpdif.flOrigemPedido) &&
                ValueUtil.valueEquals(nuPedido, pedidoerpdif.nuPedido);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(flOrigemPedido);
    	strBuffer.append(";");
    	strBuffer.append(nuPedido);
        return strBuffer.toString();
    }

}