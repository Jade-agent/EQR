package org.nkigen.eqr.logs;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;
import java.util.Iterator;

import java.io.IOException;
import java.io.Serializable;
import java.util.Formatter;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

public class EQRLogger  implements Serializable{

	public static final int LOG_INFO = 0;
	public static final int LOG_EERROR = 1;
	String log_path = "/home/nkigen/development/git/EQR/logs/log";
	HashMap<String, Logger> loggers;
	static EQRLogger mlog;

	protected EQRLogger() {
		loggers = new HashMap<String, Logger>();
	}

	public static Logger prep(Logger logger, String agent) {
		// TODO Auto-generated constructor stub
		if (mlog == null) {
			mlog = new EQRLogger();
			logger = Logger.getJADELogger(agent);
			try {
				Handler handler = new FileHandler(mlog.log_path + "_" + agent+".html",
						false);
				EQRHTMLFormatter html = new EQRHTMLFormatter(agent);
				handler.setFormatter(html);
				logger.addHandler(handler);
				mlog.loggers.put(agent, logger);

			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return logger;
		}

		if (mlog.loggers.containsKey(agent)) {
			return mlog.loggers.get(agent);

		} else {
			logger = Logger.getJADELogger(agent);
			try {
				Handler handler = new FileHandler(mlog.log_path + "_" + agent+".html",
						false);
				EQRHTMLFormatter html = new EQRHTMLFormatter(agent);
				handler.setFormatter(html);
				logger.addHandler(handler);
				mlog.loggers.put(agent, logger);

			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return logger;

	}

	public static void log(Logger logger, ACLMessage msg, String agent,
			String comments) {
		String log = "";
		if (msg == null) {
			log = "<td>-</td><td>-</td><td>-</td><td>-</td><td>" + comments
					+ "</td>";
		} else {
			log = "<td>" + getACLMessageName(msg) + "</td><td>";
			if (msg.getSender() == null)
				log += "-";
			else
				log += msg.getSender().getLocalName();
			log += "</td><td>";
			if (msg.getAllReceiver().hasNext())
				log += allReceivers(msg);
			else
				log += "-";
			log += "</td><td>" + getContentType(msg) + "</td><td>" + comments
					+ "</td>";
		}
		logger.info(log);
	}

	private static String allReceivers(ACLMessage msg) {
		String recv = "";
		Iterator lst = msg.getAllReceiver();
		while (lst.hasNext()) {
			recv += ((AID) lst.next()).getLocalName()+" : ";
		}
		return recv;
	}

	public static void log(int type, Logger logger, ACLMessage msg,
			String agent, String comments) {
		String log = "";
		if (msg == null) {
			log = "<td>-</td><td>-</td><td>-</td><td>-</td><td>" + comments
					+ "</td>";
		} else {
			log = "<td>" + getACLMessageName(msg) + "</td><td>";
			if (msg.getSender() == null)
				log += "-";
			else
				log += msg.getSender().getLocalName();
			log += "</td><td>";
			if (msg.getAllReceiver().hasNext())
				log += allReceivers(msg);
			else
				log += "-";
			log += "</td><td>" + getContentType(msg) + "</td><td>" + comments
					+ "</td>";
		}
		switch (type) {
		case LOG_INFO:
			logger.info(log);
			break;
		case LOG_EERROR:
			logger.log(Level.WARNING, log);
			break;
		}
	}

	private static Class<? extends Object> getContentType(ACLMessage msg) {
		try {
			Object content = msg.getContentObject();
			if (content != null)
				return content.getClass();

		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getACLMessageName(ACLMessage msg) {
		switch (msg.getPerformative()) {
		case ACLMessage.INFORM:
			return "INFORM";
		case ACLMessage.FAILURE:
			return "FAILURE";
		case ACLMessage.REQUEST:
			return "REQUEST";
		case ACLMessage.CONFIRM:
			return "CONFIRM";
		case ACLMessage.PROPAGATE:
			return "PROPAGATE";
		default:
			return "UNKNOWN";
		}
	}

}
