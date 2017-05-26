package com.mobileeftpos.android.eftpos.utils;


/**
 * Created by Prathap on 4/26/17.
 */

/**
 * NetworkConnectionState: This enum class for Network state.
 *
  */

public enum NetworkConnectionState {
	/**
	 * CONNECTING: This variable will use for connecting status of network.
	 */
	CONNECTING(1),
	/**
	 * CONNECTED: This variable will use for connected status of network.
	 */
	CONNECTED(2),
	/**
	 * DISCONNECTED: This variable will use for disconnected status of network.
	 */
	DISCONNECTED(3);

	/**
	 * Id: This variable will variable id.
	 */
	private int id;

	/**
	 * SyncStatus constructor.
	 */
	NetworkConnectionState(int id) {
		this.id = id;
	}

	/**
	 * getId: This is the method for getting id.
	 */
	public int getId() {
		return this.id;
	}
}
