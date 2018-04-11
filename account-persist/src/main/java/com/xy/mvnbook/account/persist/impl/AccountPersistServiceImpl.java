package com.xy.mvnbook.account.persist.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.xy.mvnbook.account.persist.AccountPersistService;
import com.xy.mvnbook.account.persist.constants.AccountPersist;
import com.xy.mvnbook.account.persist.dto.Account;
import com.xy.mvnbook.account.persist.exception.AccountPersistException;

import lombok.Data;
/**
 * 持久化 实现
 *
 * @version 0.1
 * @author xy
 * @date 2018年3月25日 下午6:09:54
 */
@Data
public class AccountPersistServiceImpl implements AccountPersistService {
    
    /**
     * 文件存储地址
     */
    private String fileUrl;
    /**
     * dom4j reader
     */
    private SAXReader reader = new SAXReader();

    public Account createAccount(Account account) throws AccountPersistException {
        Document doc = this.readDocument();
        Element accountsEle = doc.getRootElement().element(AccountPersist.ELEMENT_ACCOUNTS);
        Element accountEle = accountsEle.addElement(AccountPersist.ELEMENT_ACCOUNT);
        accountEle.addElement(AccountPersist.ELEMENT_ACCOUNT_ID).addText(account.getId());
        accountEle.addElement(AccountPersist.ELEMENT_ACCOUNT_NAME).addText(account.getName());
        accountEle.addElement(AccountPersist.ELEMENT_ACCOUNT_PASSWORD).addText(account.getPassword());
        accountEle.addElement(AccountPersist.ELEMENT_ACCOUNT_EMAIL).addText(account.getEmail());
        accountEle.addElement(AccountPersist.ELEMENT_ACCOUNT_ACTIVATED).addText(account.isActivated() ? "true" : "false");
        this.writeDocument(doc);
        return account;
    }

    public boolean deleteAccount(String id) throws AccountPersistException {
        Document doc = this.readDocument();
        Element accountsEle = doc.getRootElement().element(AccountPersist.ELEMENT_ACCOUNTS);
        @SuppressWarnings("unchecked")
        Iterator<Element> ei = accountsEle.elementIterator();
        while(ei.hasNext()) {
            Element next = ei.next();
            if (next.elementText(AccountPersist.ELEMENT_ACCOUNT_ID).equals(id)) {
                accountsEle.remove(next);
                return true;
            }
        }

        return false;
    }

    public Account updateAccount(Account account) throws AccountPersistException {
        Document doc = this.readDocument();
        Element accountsEle = doc.getRootElement().element(AccountPersist.ELEMENT_ACCOUNTS);
        @SuppressWarnings("unchecked")
        Iterator<Element> ei = accountsEle.elementIterator();
        Account old  = null;
        while(ei.hasNext()) {
            Element next = ei.next();
            if (next.elementText(AccountPersist.ELEMENT_ACCOUNT_ID).equals(account.getId())) {
                old = this.buildAccount(next);
                if (account.getEmail() != null && !"".equals(account.getEmail())) {
                    next.element(AccountPersist.ELEMENT_ACCOUNT_EMAIL).addText(account.getEmail());
                }
                if (account.getName() != null && !"".equals(account.getName())) {
                    next.element(AccountPersist.ELEMENT_ACCOUNT_NAME).addText(account.getName());
                }
                if (account.getPassword() != null && !"".equals(account.getPassword())) {
                    next.element(AccountPersist.ELEMENT_ACCOUNT_PASSWORD).addText(account.getPassword());
                }
                if (account.isActivated()) {
                    next.element(AccountPersist.ELEMENT_ACCOUNT_ACTIVATED).addText("true");
                }
            }
        }
        return old;
    }

    public Account readAccount(String id) throws AccountPersistException {
        Document doc = readDocument();
        Element accounts = doc.getRootElement().element(AccountPersist.ELEMENT_ACCOUNTS);
        @SuppressWarnings("unchecked")
        List<Element> elements = accounts.elements();
        for (Element ele : elements) {
            if (ele.elementText(AccountPersist.ELEMENT_ACCOUNT_ID).equals(id)) {
                return buildAccount(ele);
            }
        }
        return null;
    }
    
    private Account buildAccount(Element ele) {
        Account account = new Account();
        account.setActivated("true".equals(ele.elementText(AccountPersist.ELEMENT_ACCOUNT_ACTIVATED)) ? true : false);
        account.setEmail(ele.elementText(AccountPersist.ELEMENT_ACCOUNT_EMAIL));
        account.setId(ele.elementText(AccountPersist.ELEMENT_ACCOUNT_ID));
        account.setName(ele.elementText(AccountPersist.ELEMENT_ACCOUNT_NAME));
        account.setPassword(ele.elementText(AccountPersist.ELEMENT_ACCOUNT_PASSWORD));
        return account;
    }

    /**
     * read document
     * 
     * @return
     * @throws AccountPersistException
     */
    private Document readDocument() throws AccountPersistException {
        File file = new File(fileUrl);
        // if the file doesn't exits, create it. contain basic element
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            parentFile.mkdirs();
            Document doc = DocumentFactory.getInstance().createDocument();
            doc.addElement(AccountPersist.ELEMENT_ROOT).addElement(AccountPersist.ELEMENT_ACCOUNTS);
            writeDocument(doc);
        }
        try {
            return reader.read(new File(fileUrl));
        } catch (DocumentException e) {
            throw new AccountPersistException("unable to read data.xml", e);
        }
    }
    
    private void writeDocument(Document doc) throws AccountPersistException {
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(fileUrl), "utf-8");
            XMLWriter xmlWriter = new XMLWriter(osw, OutputFormat.createPrettyPrint());
            xmlWriter.write(doc);
        } catch (IOException e) {
            throw new AccountPersistException("unable to write data.xml", e);
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    throw new AccountPersistException("unable to close data.xml", e);
                }
            }
        }
    }

}
