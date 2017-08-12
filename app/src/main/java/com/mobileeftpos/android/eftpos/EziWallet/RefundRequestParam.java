package com.mobileeftpos.android.eftpos.EziWallet;

public class RefundRequestParam {
	private String eziOrderNo;

	private String merchantId;

	private Long storeId;

	private String sign;


	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}


	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

    public String getEziOrderNo() {
        return eziOrderNo;
    }

    public void setEziOrderNo(String eziOrderNo) {
        this.eziOrderNo = eziOrderNo;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}


