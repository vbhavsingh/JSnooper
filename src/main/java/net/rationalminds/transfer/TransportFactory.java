package net.rationalminds.transfer;

import net.rationalminds.dto.CommonTransactionModel;
import net.rationalminds.transfer.servlet.ServletTransfer;
import net.rationalminds.util.CONSTANTS;
import net.rationalminds.util.PropertyManager;

public class TransportFactory {

	public static TransferItf getInstance(CommonTransactionModel e2eTx) {

		TransferItf transferObj = null;

		/**
		 * if transport mode is servlet use web servlet impl.
		 */
		if (CONSTANTS.TRANSPORT_SERVLET.equals(PropertyManager.TRANSPORT_MODE)) {
			transferObj = new ServletTransfer(e2eTx);
		}
		return transferObj;
	}

}
