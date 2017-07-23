/*
 * jPOS Project [http://jpos.org]
 * Copyright (C) 2000-2016 Alejandro P. Revilla
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mobileeftpos.android.eftpos.SupportClasses;

import org.jpos.iso.*;

/**
 * ISO 8583 v1987 BINARY Packager
 *
 * @author apr@cs.com.uy
 * @version $Id$
 * @see ISOPackager
 * @see ISOBasePackager
 * @see ISOComponent
 */
public class ISOPackager1 extends ISOBasePackager {
	private static final boolean pad = false;
	protected ISOFieldPackager fld[] = { /* 000 */ new IFB_NUMERIC(4, "Message Type Indicator", pad),
			/* 001 */ new IFB_BITMAP(8, "Bitmap"), /* 002 */ new IFB_LLNUM(19, "Primary Account number  ", pad),
			/* 003 */ new IFB_NUMERIC(6, "Processing Code", pad),
			/* 004 */ new IFB_NUMERIC(12, "Amount, Transaction", pad),
			/* 005 */ new IFB_NUMERIC(12, "Amount, Reconciliation", pad),
			/* 006 */ new IFB_NUMERIC(12, "Amount, Cardholder billing", pad),
			/* 007 */ new IFB_NUMERIC(10, "Date and time, transmission", pad), /* 008 */ new IFB_NUMERIC(8, "	", pad),
			/* 009 */ new IFB_NUMERIC(8, "Conversion rate, Reconciliation", pad),
			/* 010 */ new IFB_NUMERIC(8, "Conversion rate, Cardholder billing", pad),
			/* 011 */ new IFB_NUMERIC(6, "Systems trace audit number", pad),
			/* 012 */ new IFB_NUMERIC(6, "Date and time, Local transaction", pad),
			/* 013 */ new IFB_NUMERIC(4, "Date, Effective", pad), /* 014 */ new IFB_NUMERIC(4, "Date, Expiration", pad),
			/* 015 */ new IFB_NUMERIC(6, "Date, Settlement", pad),
			/* 016 */ new IFB_NUMERIC(4, "Date, Conversion", pad), /* 017 */ new IFB_NUMERIC(4, "Date, Capture", pad),
			/* 018 */ new IFB_NUMERIC(4, "Merchant type", pad),
			/* 019 */ new IFB_NUMERIC(3, "Country code, Acquiring institution", pad),
			/* 020 */ new IFB_NUMERIC(3, "Country code, Primary account number", pad),
			/* 021 */ new IFB_NUMERIC(3, "Country code, Forwarding institution", pad),
			/* 022 */ new IF_CHAR(3, "Point of service data code"),
			/* 023 */ new IFB_NUMERIC(3, "Card sequence number", pad),
			/* 024 */ new IFB_NUMERIC(4, "Function code", pad),
			/* 025 */ new IFB_NUMERIC(2, "Message reason code", pad),
			/* 026 */ new IFB_LLLCHAR(999, "TMS File Name "), // Venkat
			/* 027 */ new IFB_LLLCHAR(256, "application name ; separator"),
			/* 028 */ new IFB_LLLCHAR(64, "number of parts available for download"),
			/* 029 */ new IFB_LLLCHAR(20, "part of file to download"),
			/* 030 */ new IFB_LLLCHAR(512, "tms change date"), /* 031 */ new IFB_LLLCHAR(999, "file data"),
			/* 032 */ new IFB_LLNUM(11, "Acquirer institution ident code", pad),
			/* 033 */ new IFB_LLLCHAR(256, " date anf time for tms"),
			/* 034 */ new IFB_LLCHAR(28, "Primary account number, extended"),
			/* 035 */ new IFB_LLCHAR(37, "Track 2 data"), /* 036 */ new IFB_LLLCHAR(104, "Track 3 data"),
			/* 037 */ new IF_CHAR(12, "Retrieval reference number"), /* 038 */ new IF_CHAR(6, "Approval code"),
			/* 039 */ new IF_CHAR(2, "Action code"), /* 040 */ new IFB_NUMERIC(3, "Service code", pad),
			/* 041 */ new IF_CHAR(8, "Card acceptor terminal identification"),
			/* 042 */ new IF_CHAR(15, "Card acceptor identification code"),
			/* 043 */ new IFB_LLCHAR(99, "Card acceptor name/location"),
			/* 044 */ new IFB_LLCHAR(99, "Additional response data"), /* 045 */ new IFB_LLCHAR(76, "Track 1 data"),
			/* 046 */ new IFB_LLLCHAR(204, "Amounts, Fees"),
			/* 047 */ new IFB_LLCHAR(99, "Additional data - national"),
			/* 048 */ new IFB_LLLCHAR(999, "Additional data - private"),
			/* 049 */ new IF_CHAR(3, "Currency code, Transaction"),
			/* 050 */ new IF_CHAR(3, "Currency code, Reconciliation"),
			/* 051 */ new IF_CHAR(3, "Currency code, Cardholder billing"),
			/* 052 */ new IFB_BINARY(8, "Personal identification number (PIN) data"),
			/* 053 */ new IFB_LLBINARY(48, "Security related control information"),
			/* 054 */ new IFB_LLLCHAR(120, "Amounts, additional"),
			/* 055 */ new IFB_LLLBINARY(255, "IC card system related data"),
			/* 056 */ new IFB_LLNUM(35, "Original data elements", pad),
			/* 057 */ new IFB_NUMERIC(3, "Authorization life cycle code", pad),
			/* 058 */ new IFB_LLNUM(11, "Authorizing agent institution Id Code", pad),
			/* 059 */ new IFB_LLLCHAR(999, "Transport data"),
			/* 060 */ new IFB_LLLCHAR(999, "Reserved for national use"),
			/* 061 */ new IFB_LLLCHAR(999, "Reserved for national use"),
			/* 062 */ new IFB_LLLCHAR(999, "Reserved for private use"),
			/* 063 */ new IFB_LLLCHAR(999, "Reserved for private use"),
			/* 064 */ new IFB_BINARY(8, "Message authentication code field"), };

	public ISOPackager1() {
		super();
		setFieldPackager(fld);
	}
}
