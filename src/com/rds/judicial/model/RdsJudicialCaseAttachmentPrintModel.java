package com.rds.judicial.model;

import org.apache.commons.lang.StringUtils;

import lombok.Data;

public class RdsJudicialCaseAttachmentPrintModel {
            private String case_id;
            private String case_code;
            private String cardId;
            private String id_card_path;
            private String id_card_date;
            private String id;
            private String pic_path;
            private String pic_date;
            private String reg_path;
            private String reg_date;
            private int reg_print=0;
            private int id_card_print=0;
            private int pic_print=0;
            
			public String getReg_path() {
				if(StringUtils.isNotEmpty(reg_path)){
					return reg_path.replace("\\", "\\\\");
				}
				return reg_path;
			}
			public void setReg_path(String reg_path) {
				this.reg_path = reg_path;
			}
			public String getReg_date() {
				return reg_date;
			}
			public void setReg_date(String reg_date) {
				this.reg_date = reg_date;
			}
			public int getReg_print() {
				return reg_print;
			}
			public void setReg_print(int reg_print) {
				this.reg_print = reg_print;
			}
			public String getCase_id() {
				return case_id;
			}
			public void setCase_id(String case_id) {
				this.case_id = case_id;
			}
			public String getCase_code() {
				return case_code;
			}
			public void setCase_code(String case_code) {
				this.case_code = case_code;
			}
			public String getCardId() {
				return cardId;
			}
			public void setCardId(String cardId) {
				this.cardId = cardId;
			}
			public String getId_card_path() {
				if(StringUtils.isNotEmpty(id_card_path)){
					return id_card_path.replace("\\", "\\\\");
				}
				return id_card_path;
			}
			public void setId_card_path(String id_card_path) {
				this.id_card_path = id_card_path;
			}
			public String getId_card_date() {
				return id_card_date;
			}
			public void setId_card_date(String id_card_date) {
				this.id_card_date = id_card_date;
			}
			public String getPic_path() {
				if(StringUtils.isNotEmpty(pic_path)){
					return pic_path.replace("\\", "\\\\");
				}
				return pic_path;
			}
			public void setPic_path(String pic_path) {
				this.pic_path = pic_path;
			}
			public String getPic_date() {
				return pic_date;
			}
			public void setPic_date(String pic_date) {
				this.pic_date = pic_date;
			}
			public int getId_card_print() {
				return id_card_print;
			}
			public void setId_card_print(int id_card_print) {
				this.id_card_print = id_card_print;
			}
			public int getPic_print() {
				return pic_print;
			}
			public void setPic_print(int pic_print) {
				this.pic_print = pic_print;
			}
			public String getId() {
				return id;
			}
			public void setId(String id) {
				this.id = id;
			}
            
            
}
