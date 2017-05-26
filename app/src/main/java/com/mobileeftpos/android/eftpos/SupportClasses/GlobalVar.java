package com.mobileeftpos.android.eftpos.SupportClasses;

/**
 * Created by venkat on 5/18/2017.
 */
public class GlobalVar {

	public GlobalVar() {
		this.tmsParam = new TmsParam();
	}

	private int inGTransactionType;

	public int getGTransactionType() {
		return inGTransactionType;
	}

	public void setGTransactionType(int inGTransactionType) {
		this.inGTransactionType = inGTransactionType;
	}

	// TMS Download Settings
	private int inGPartToDownload;
	private int inGTotalNumberofTMSparts;
	private String filename; // terminal id or parameter file name
	private String directory; // path of application example: TELIUM/DBS or
								// MAGIC3/DBS
	private String ActivationDate;// yymmddhhmm Date of when parameter was
									// modified in MMS system
	private String LastCheckedDate;// yymmddhhmmss Date of when terminal last
									// checked if there are changes in the MMS
									// system
	private String TotalParts;// number of parts we need to download
	private String PartsDownloaded; // number of parts we have downloaded so
	public String TmsData = "";
	// far.

	public void setfilename(String filename) {
		this.filename = filename;
	}

	public String getfilename() {
		return filename;
	}

	public void setdirectory(String directory) {
		this.directory = directory;
	}

	public String getdirectory() {
		return directory;
	}

	public void setActivationDate(String ActivationDate) {
		this.ActivationDate = ActivationDate;
	}

	public String getActivationDate() {
		return ActivationDate;
	}

	public void setLastCheckedDate(String LastCheckedDate) {
		this.LastCheckedDate = LastCheckedDate;
	}

	public String getLastCheckedDate() {
		return LastCheckedDate;
	}

	public void setTotalParts(String TotalParts) {
		this.TotalParts = TotalParts;
	}

	public String getTotalParts() {
		return TotalParts;
	}

	public void setPartsDownloaded(String PartsDownloaded) {
		this.PartsDownloaded = PartsDownloaded;
	}

	public String getPartsDownloaded() {
		return PartsDownloaded;
	}

	public int getGPartToDownload() {
		return inGPartToDownload;
	}

	public void setGTotalNumberofTMSparts(int inGTotalNumberofTMSparts) {
		this.inGTotalNumberofTMSparts = inGTotalNumberofTMSparts;
	}

	public int getGTotalNumberofTMSparts() {
		return inGTotalNumberofTMSparts;
	}

	public void setGPartToDownload(int inGPartToDownload) {
		this.inGPartToDownload = inGPartToDownload;
	}

	public TmsParam tmsParam;

	public class TmsParam {
		private String TMS_APPLICATION = "DBS";
		private String TMS_FILENAME = "17200143";
		private String TMS_TERMINAL_ID = "17200143";
		private String TMS_CONNECTION_TYPE = "ETHERNET";
		private String TMS_CONNECTION_TO = "OFFICE NETWORK";
		private String TMS_TPDU = "6007000000";

		public String getTMS_APPLICATION() {
			return TMS_APPLICATION;
		}

		public void setTMS_APPLICATION(String TMS_APPLICATION) {
			this.TMS_APPLICATION = TMS_APPLICATION;
		}

		public String getTMS_FILENAME() {
			return TMS_FILENAME;
		}

		public void setTMS_FILENAME(String TMS_FILENAME) {
			this.TMS_FILENAME = TMS_FILENAME;
		}

		public String getTMS_TERMINAL_ID() {
			return TMS_TERMINAL_ID;
		}

		public void setTMS_TERMINAL_ID(String TMS_TERMINAL_ID) {
			this.TMS_TERMINAL_ID = TMS_TERMINAL_ID;
		}

		public String getTMS_TPDU() {
			return TMS_TPDU;
		}

		public void setTMS_TPDU(String TMS_TPDU) {
			this.TMS_TPDU = TMS_TPDU;
		}

		public String getTMS_CONNECTION_TYPE() {
			return TMS_CONNECTION_TYPE;
		}

		public void setTMS_CONNECTION_TYPE(String TMS_CONNECTION_TYPE) {
			this.TMS_CONNECTION_TYPE = TMS_CONNECTION_TYPE;
		}

		public String getTMS_CONNECTION_TO() {
			return TMS_CONNECTION_TO;
		}

		public void setTMS_CONNECTION_TO(String TMS_CONNECTION_TO) {
			this.TMS_CONNECTION_TO = TMS_CONNECTION_TO;
		}
	}

}
