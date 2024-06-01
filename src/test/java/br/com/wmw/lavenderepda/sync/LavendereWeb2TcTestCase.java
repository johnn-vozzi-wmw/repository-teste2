package br.com.wmw.lavenderepda.sync;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.util.ValueUtil;
import static org.junit.jupiter.api.Assertions.*;
import br.com.wmw.lavenderepda.business.domain.dto.RetornoPedidoDTO;
import org.junit.jupiter.api.Test;
import totalcross.json.JSONException;

public class LavendereWeb2TcTestCase  {

	@Test
	public void testGetNewRetornoPedidoDTOErro() throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, JSONException {
		LavendereWeb2Tc lavendereWeb2Tc = new LavendereWeb2Tc(new ParamsSync());
		RetornoPedidoDTO retornoPedidoDTO = lavendereWeb2Tc.getNewRetornoPedidoDTO("{\"erroRetorno\":\"Falha na geração da Nfe\"}");
		String erroRetorno = lavendereWeb2Tc.getErroRetorno(retornoPedidoDTO);
		assertTrue(ValueUtil.isNotEmpty(erroRetorno));
		assertTrue(erroRetorno.contains("Falha na geração da Nfe"));
		
		retornoPedidoDTO = new RetornoPedidoDTO();
		erroRetorno = lavendereWeb2Tc.getErroRetorno(retornoPedidoDTO);
		assertTrue(ValueUtil.isEmpty(erroRetorno));

	}

}
