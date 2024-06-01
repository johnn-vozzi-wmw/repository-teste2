package br.com.wmw.lavenderepda.business.domain.dto;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.lavenderepda.business.domain.DescProgressivoConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;

public class ItemPedidoDescontoModificadoDTO extends BaseDomain {

    public ItemPedido itemPedido;
    public double vlPctDescProgAnterior;
    public double vlPctDescProgNovo;
    public String cdDescProgAnterior;
    public String cdDescProgNovo;

    public DescProgressivoConfig descProgressivoAnterior;
    public DescProgressivoConfig descProgressivoNovo;

    public ItemPedidoDescontoModificadoDTO() {
    }

    public ItemPedidoDescontoModificadoDTO(ItemPedido itemPedido,
										   double vlPctDescProgAnterior,
										   double vlPctDescProgNovo,
										   String cdDescProgAnterior,
										   String cdDescProgNovo,
                                           DescProgressivoConfig descProgressivoAnterior,
                                           DescProgressivoConfig descProgressivoNovo) {
        this.itemPedido = itemPedido;
        this.vlPctDescProgAnterior = vlPctDescProgAnterior;
        this.vlPctDescProgNovo = vlPctDescProgNovo;
        this.cdDescProgAnterior = cdDescProgAnterior;
        this.cdDescProgNovo = cdDescProgNovo;
        this.descProgressivoAnterior = descProgressivoAnterior;
        this.descProgressivoNovo = descProgressivoNovo;
    }

    @Override
    public String getPrimaryKey() {
        return null;
    }

}
