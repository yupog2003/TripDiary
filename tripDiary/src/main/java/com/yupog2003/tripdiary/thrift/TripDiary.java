package com.yupog2003.tripdiary.thrift;

import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;
import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.server.AbstractNonblockingServer.AsyncFrameBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TripDiary {

	public interface Iface {

		public List<Trip> getTrips(String token, boolean isPublic, String author, int section) throws org.apache.thrift.TException;

		public Trip getTrip(String token, String tripPath) throws org.apache.thrift.TException;

		public List<Post> getPosts(String token, String tripPath) throws org.apache.thrift.TException;

		public String getGpx(String token, String tripPath) throws org.apache.thrift.TException;

		public String zipTrip(String token, String tripPath) throws org.apache.thrift.TException;

		public boolean edit_trip_note(String token, String tripPath, String note) throws org.apache.thrift.TException;

		public boolean edi_poi_diary(String token, String tripPath, String poiName, String diary) throws org.apache.thrift.TException;

		public boolean edi_poi_basicinformation(String token, String tripPath, String poiName, String content) throws org.apache.thrift.TException;

		public boolean delete_trip(String token, String tripPath) throws org.apache.thrift.TException;

		public boolean rename_poi_file(String token, String path, String newName) throws org.apache.thrift.TException;

		public boolean rename_poi(String token, String tripPath, String poiName, String newPOIName) throws org.apache.thrift.TException;

		public boolean rename_trip(String token, String trippath, String newTripPath) throws org.apache.thrift.TException;

		public boolean add_poi(String token, String tripPath, String poiName, String basicinformation) throws org.apache.thrift.TException;

		public boolean toggle_public(String token, String tripPath, String option) throws org.apache.thrift.TException;

		public boolean add_like(String token, String tripPath, String who) throws org.apache.thrift.TException;

		public boolean delete_like(String token, String tripPath, String who) throws org.apache.thrift.TException;

		public boolean add_post(String token, String tripPath, String who, String content) throws org.apache.thrift.TException;

		public boolean add_view(String tripPath) throws org.apache.thrift.TException;

		public boolean check_trip_public(String tripPath) throws org.apache.thrift.TException;

	}

	public interface AsyncIface {

		public void getTrips(String token, boolean isPublic, String author, int section, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void getTrip(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void getPosts(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void getGpx(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void zipTrip(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void edit_trip_note(String token, String tripPath, String note, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void edi_poi_diary(String token, String tripPath, String poiName, String diary, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void edi_poi_basicinformation(String token, String tripPath, String poiName, String content, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void delete_trip(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void rename_poi_file(String token, String path, String newName, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void rename_poi(String token, String tripPath, String poiName, String newPOIName, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void rename_trip(String token, String trippath, String newTripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void add_poi(String token, String tripPath, String poiName, String basicinformation, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void toggle_public(String token, String tripPath, String option, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void add_like(String token, String tripPath, String who, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void delete_like(String token, String tripPath, String who, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void add_post(String token, String tripPath, String who, String content, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void add_view(String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

		public void check_trip_public(String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException;

	}

	public static class Client extends org.apache.thrift.TServiceClient implements Iface {
		public static class Factory implements org.apache.thrift.TServiceClientFactory<Client> {
			public Factory() {
			}

			public Client getClient(org.apache.thrift.protocol.TProtocol prot) {
				return new Client(prot);
			}

			public Client getClient(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
				return new Client(iprot, oprot);
			}
		}

		public Client(org.apache.thrift.protocol.TProtocol prot) {
			super(prot, prot);
		}

		public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
			super(iprot, oprot);
		}

		public List<Trip> getTrips(String token, boolean isPublic, String author, int section) throws org.apache.thrift.TException {
			send_getTrips(token, isPublic, author, section);
			return recv_getTrips();
		}

		public void send_getTrips(String token, boolean isPublic, String author, int section) throws org.apache.thrift.TException {
			getTrips_args args = new getTrips_args();
			args.setToken(token);
			args.setIsPublic(isPublic);
			args.setAuthor(author);
			args.setSection(section);
			sendBase("getTrips", args);
		}

		public List<Trip> recv_getTrips() throws org.apache.thrift.TException {
			getTrips_result result = new getTrips_result();
			try {
				receiveBase(result, "getTrips");
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "getTrips failed: unknown result");
		}

		public Trip getTrip(String token, String tripPath) throws org.apache.thrift.TException {
			send_getTrip(token, tripPath);
			return recv_getTrip();
		}

		public void send_getTrip(String token, String tripPath) throws org.apache.thrift.TException {
			getTrip_args args = new getTrip_args();
			args.setToken(token);
			args.setTripPath(tripPath);
			sendBase("getTrip", args);
		}

		public Trip recv_getTrip() throws org.apache.thrift.TException {
			getTrip_result result = new getTrip_result();
			receiveBase(result, "getTrip");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "getTrip failed: unknown result");
		}

		public List<Post> getPosts(String token, String tripPath) throws org.apache.thrift.TException {
			send_getPosts(token, tripPath);
			return recv_getPosts();
		}

		public void send_getPosts(String token, String tripPath) throws org.apache.thrift.TException {
			getPosts_args args = new getPosts_args();
			args.setToken(token);
			args.setTripPath(tripPath);
			sendBase("getPosts", args);
		}

		public List<Post> recv_getPosts() throws org.apache.thrift.TException {
			getPosts_result result = new getPosts_result();
			receiveBase(result, "getPosts");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "getPosts failed: unknown result");
		}

		public String getGpx(String token, String tripPath) throws org.apache.thrift.TException {
			send_getGpx(token, tripPath);
			return recv_getGpx();
		}

		public void send_getGpx(String token, String tripPath) throws org.apache.thrift.TException {
			getGpx_args args = new getGpx_args();
			args.setToken(token);
			args.setTripPath(tripPath);
			sendBase("getGpx", args);
		}

		public String recv_getGpx() throws org.apache.thrift.TException {
			getGpx_result result = new getGpx_result();
			receiveBase(result, "getGpx");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "getGpx failed: unknown result");
		}

		public String zipTrip(String token, String tripPath) throws org.apache.thrift.TException {
			send_zipTrip(token, tripPath);
			return recv_zipTrip();
		}

		public void send_zipTrip(String token, String tripPath) throws org.apache.thrift.TException {
			zipTrip_args args = new zipTrip_args();
			args.setToken(token);
			args.setTripPath(tripPath);
			sendBase("zipTrip", args);
		}

		public String recv_zipTrip() throws org.apache.thrift.TException {
			zipTrip_result result = new zipTrip_result();
			receiveBase(result, "zipTrip");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "zipTrip failed: unknown result");
		}

		public boolean edit_trip_note(String token, String tripPath, String note) throws org.apache.thrift.TException {
			send_edit_trip_note(token, tripPath, note);
			return recv_edit_trip_note();
		}

		public void send_edit_trip_note(String token, String tripPath, String note) throws org.apache.thrift.TException {
			edit_trip_note_args args = new edit_trip_note_args();
			args.setToken(token);
			args.setTripPath(tripPath);
			args.setNote(note);
			sendBase("edit_trip_note", args);
		}

		public boolean recv_edit_trip_note() throws org.apache.thrift.TException {
			edit_trip_note_result result = new edit_trip_note_result();
			receiveBase(result, "edit_trip_note");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "edit_trip_note failed: unknown result");
		}

		public boolean edi_poi_diary(String token, String tripPath, String poiName, String diary) throws org.apache.thrift.TException {
			send_edi_poi_diary(token, tripPath, poiName, diary);
			return recv_edi_poi_diary();
		}

		public void send_edi_poi_diary(String token, String tripPath, String poiName, String diary) throws org.apache.thrift.TException {
			edi_poi_diary_args args = new edi_poi_diary_args();
			args.setToken(token);
			args.setTripPath(tripPath);
			args.setPoiName(poiName);
			args.setDiary(diary);
			sendBase("edi_poi_diary", args);
		}

		public boolean recv_edi_poi_diary() throws org.apache.thrift.TException {
			edi_poi_diary_result result = new edi_poi_diary_result();
			receiveBase(result, "edi_poi_diary");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "edi_poi_diary failed: unknown result");
		}

		public boolean edi_poi_basicinformation(String token, String tripPath, String poiName, String content) throws org.apache.thrift.TException {
			send_edi_poi_basicinformation(token, tripPath, poiName, content);
			return recv_edi_poi_basicinformation();
		}

		public void send_edi_poi_basicinformation(String token, String tripPath, String poiName, String content) throws org.apache.thrift.TException {
			edi_poi_basicinformation_args args = new edi_poi_basicinformation_args();
			args.setToken(token);
			args.setTripPath(tripPath);
			args.setPoiName(poiName);
			args.setContent(content);
			sendBase("edi_poi_basicinformation", args);
		}

		public boolean recv_edi_poi_basicinformation() throws org.apache.thrift.TException {
			edi_poi_basicinformation_result result = new edi_poi_basicinformation_result();
			receiveBase(result, "edi_poi_basicinformation");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "edi_poi_basicinformation failed: unknown result");
		}

		public boolean delete_trip(String token, String tripPath) throws org.apache.thrift.TException {
			send_delete_trip(token, tripPath);
			return recv_delete_trip();
		}

		public void send_delete_trip(String token, String tripPath) throws org.apache.thrift.TException {
			delete_trip_args args = new delete_trip_args();
			args.setToken(token);
			args.setTripPath(tripPath);
			sendBase("delete_trip", args);
		}

		public boolean recv_delete_trip() throws org.apache.thrift.TException {
			delete_trip_result result = new delete_trip_result();
			receiveBase(result, "delete_trip");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "delete_trip failed: unknown result");
		}

		public boolean rename_poi_file(String token, String path, String newName) throws org.apache.thrift.TException {
			send_rename_poi_file(token, path, newName);
			return recv_rename_poi_file();
		}

		public void send_rename_poi_file(String token, String path, String newName) throws org.apache.thrift.TException {
			rename_poi_file_args args = new rename_poi_file_args();
			args.setToken(token);
			args.setPath(path);
			args.setNewName(newName);
			sendBase("rename_poi_file", args);
		}

		public boolean recv_rename_poi_file() throws org.apache.thrift.TException {
			rename_poi_file_result result = new rename_poi_file_result();
			receiveBase(result, "rename_poi_file");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "rename_poi_file failed: unknown result");
		}

		public boolean rename_poi(String token, String tripPath, String poiName, String newPOIName) throws org.apache.thrift.TException {
			send_rename_poi(token, tripPath, poiName, newPOIName);
			return recv_rename_poi();
		}

		public void send_rename_poi(String token, String tripPath, String poiName, String newPOIName) throws org.apache.thrift.TException {
			rename_poi_args args = new rename_poi_args();
			args.setToken(token);
			args.setTripPath(tripPath);
			args.setPoiName(poiName);
			args.setNewPOIName(newPOIName);
			sendBase("rename_poi", args);
		}

		public boolean recv_rename_poi() throws org.apache.thrift.TException {
			rename_poi_result result = new rename_poi_result();
			receiveBase(result, "rename_poi");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "rename_poi failed: unknown result");
		}

		public boolean rename_trip(String token, String trippath, String newTripPath) throws org.apache.thrift.TException {
			send_rename_trip(token, trippath, newTripPath);
			return recv_rename_trip();
		}

		public void send_rename_trip(String token, String trippath, String newTripPath) throws org.apache.thrift.TException {
			rename_trip_args args = new rename_trip_args();
			args.setToken(token);
			args.setTrippath(trippath);
			args.setNewTripPath(newTripPath);
			sendBase("rename_trip", args);
		}

		public boolean recv_rename_trip() throws org.apache.thrift.TException {
			rename_trip_result result = new rename_trip_result();
			receiveBase(result, "rename_trip");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "rename_trip failed: unknown result");
		}

		public boolean add_poi(String token, String tripPath, String poiName, String basicinformation) throws org.apache.thrift.TException {
			send_add_poi(token, tripPath, poiName, basicinformation);
			return recv_add_poi();
		}

		public void send_add_poi(String token, String tripPath, String poiName, String basicinformation) throws org.apache.thrift.TException {
			add_poi_args args = new add_poi_args();
			args.setToken(token);
			args.setTripPath(tripPath);
			args.setPoiName(poiName);
			args.setBasicinformation(basicinformation);
			sendBase("add_poi", args);
		}

		public boolean recv_add_poi() throws org.apache.thrift.TException {
			add_poi_result result = new add_poi_result();
			receiveBase(result, "add_poi");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "add_poi failed: unknown result");
		}

		public boolean toggle_public(String token, String tripPath, String option) throws org.apache.thrift.TException {
			send_toggle_public(token, tripPath, option);
			return recv_toggle_public();
		}

		public void send_toggle_public(String token, String tripPath, String option) throws org.apache.thrift.TException {
			toggle_public_args args = new toggle_public_args();
			args.setToken(token);
			args.setTripPath(tripPath);
			args.setOption(option);
			sendBase("toggle_public", args);
		}

		public boolean recv_toggle_public() throws org.apache.thrift.TException {
			toggle_public_result result = new toggle_public_result();
			receiveBase(result, "toggle_public");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "toggle_public failed: unknown result");
		}

		public boolean add_like(String token, String tripPath, String who) throws org.apache.thrift.TException {
			send_add_like(token, tripPath, who);
			return recv_add_like();
		}

		public void send_add_like(String token, String tripPath, String who) throws org.apache.thrift.TException {
			add_like_args args = new add_like_args();
			args.setToken(token);
			args.setTripPath(tripPath);
			args.setWho(who);
			sendBase("add_like", args);
		}

		public boolean recv_add_like() throws org.apache.thrift.TException {
			add_like_result result = new add_like_result();
			receiveBase(result, "add_like");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "add_like failed: unknown result");
		}

		public boolean delete_like(String token, String tripPath, String who) throws org.apache.thrift.TException {
			send_delete_like(token, tripPath, who);
			return recv_delete_like();
		}

		public void send_delete_like(String token, String tripPath, String who) throws org.apache.thrift.TException {
			delete_like_args args = new delete_like_args();
			args.setToken(token);
			args.setTripPath(tripPath);
			args.setWho(who);
			sendBase("delete_like", args);
		}

		public boolean recv_delete_like() throws org.apache.thrift.TException {
			delete_like_result result = new delete_like_result();
			receiveBase(result, "delete_like");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "delete_like failed: unknown result");
		}

		public boolean add_post(String token, String tripPath, String who, String content) throws org.apache.thrift.TException {
			send_add_post(token, tripPath, who, content);
			return recv_add_post();
		}

		public void send_add_post(String token, String tripPath, String who, String content) throws org.apache.thrift.TException {
			add_post_args args = new add_post_args();
			args.setToken(token);
			args.setTripPath(tripPath);
			args.setWho(who);
			args.setContent(content);
			sendBase("add_post", args);
		}

		public boolean recv_add_post() throws org.apache.thrift.TException {
			add_post_result result = new add_post_result();
			receiveBase(result, "add_post");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "add_post failed: unknown result");
		}

		public boolean add_view(String tripPath) throws org.apache.thrift.TException {
			send_add_view(tripPath);
			return recv_add_view();
		}

		public void send_add_view(String tripPath) throws org.apache.thrift.TException {
			add_view_args args = new add_view_args();
			args.setTripPath(tripPath);
			sendBase("add_view", args);
		}

		public boolean recv_add_view() throws org.apache.thrift.TException {
			add_view_result result = new add_view_result();
			receiveBase(result, "add_view");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "add_view failed: unknown result");
		}

		public boolean check_trip_public(String tripPath) throws org.apache.thrift.TException {
			send_check_trip_public(tripPath);
			return recv_check_trip_public();
		}

		public void send_check_trip_public(String tripPath) throws org.apache.thrift.TException {
			check_trip_public_args args = new check_trip_public_args();
			args.setTripPath(tripPath);
			sendBase("check_trip_public", args);
		}

		public boolean recv_check_trip_public() throws org.apache.thrift.TException {
			check_trip_public_result result = new check_trip_public_result();
			receiveBase(result, "check_trip_public");
			if (result.isSetSuccess()) {
				return result.success;
			}
			throw new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.MISSING_RESULT, "check_trip_public failed: unknown result");
		}

	}

	public static class AsyncClient extends org.apache.thrift.async.TAsyncClient implements AsyncIface {
		public static class Factory implements org.apache.thrift.async.TAsyncClientFactory<AsyncClient> {
			private org.apache.thrift.async.TAsyncClientManager clientManager;
			private org.apache.thrift.protocol.TProtocolFactory protocolFactory;

			public Factory(org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.protocol.TProtocolFactory protocolFactory) {
				this.clientManager = clientManager;
				this.protocolFactory = protocolFactory;
			}

			public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport transport) {
				return new AsyncClient(protocolFactory, clientManager, transport);
			}
		}

		public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.transport.TNonblockingTransport transport) {
			super(protocolFactory, clientManager, transport);
		}

		public void getTrips(String token, boolean isPublic, String author, int section, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			getTrips_call method_call = new getTrips_call(token, isPublic, author, section, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class getTrips_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private boolean isPublic;
			private String author;
			private int section;

			public getTrips_call(String token, boolean isPublic, String author, int section, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.isPublic = isPublic;
				this.author = author;
				this.section = section;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("getTrips", org.apache.thrift.protocol.TMessageType.CALL, 0));
				getTrips_args args = new getTrips_args();
				args.setToken(token);
				args.setIsPublic(isPublic);
				args.setAuthor(author);
				args.setSection(section);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public List<Trip> getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_getTrips();
			}
		}

		public void getTrip(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			getTrip_call method_call = new getTrip_call(token, tripPath, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class getTrip_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String tripPath;

			public getTrip_call(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.tripPath = tripPath;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("getTrip", org.apache.thrift.protocol.TMessageType.CALL, 0));
				getTrip_args args = new getTrip_args();
				args.setToken(token);
				args.setTripPath(tripPath);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public Trip getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_getTrip();
			}
		}

		public void getPosts(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			getPosts_call method_call = new getPosts_call(token, tripPath, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class getPosts_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String tripPath;

			public getPosts_call(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.tripPath = tripPath;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("getPosts", org.apache.thrift.protocol.TMessageType.CALL, 0));
				getPosts_args args = new getPosts_args();
				args.setToken(token);
				args.setTripPath(tripPath);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public List<Post> getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_getPosts();
			}
		}

		public void getGpx(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			getGpx_call method_call = new getGpx_call(token, tripPath, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class getGpx_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String tripPath;

			public getGpx_call(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.tripPath = tripPath;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("getGpx", org.apache.thrift.protocol.TMessageType.CALL, 0));
				getGpx_args args = new getGpx_args();
				args.setToken(token);
				args.setTripPath(tripPath);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public String getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_getGpx();
			}
		}

		public void zipTrip(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			zipTrip_call method_call = new zipTrip_call(token, tripPath, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class zipTrip_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String tripPath;

			public zipTrip_call(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.tripPath = tripPath;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("zipTrip", org.apache.thrift.protocol.TMessageType.CALL, 0));
				zipTrip_args args = new zipTrip_args();
				args.setToken(token);
				args.setTripPath(tripPath);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public String getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_zipTrip();
			}
		}

		public void edit_trip_note(String token, String tripPath, String note, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			edit_trip_note_call method_call = new edit_trip_note_call(token, tripPath, note, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class edit_trip_note_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String tripPath;
			private String note;

			public edit_trip_note_call(String token, String tripPath, String note, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.tripPath = tripPath;
				this.note = note;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("edit_trip_note", org.apache.thrift.protocol.TMessageType.CALL, 0));
				edit_trip_note_args args = new edit_trip_note_args();
				args.setToken(token);
				args.setTripPath(tripPath);
				args.setNote(note);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public boolean getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_edit_trip_note();
			}
		}

		public void edi_poi_diary(String token, String tripPath, String poiName, String diary, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			edi_poi_diary_call method_call = new edi_poi_diary_call(token, tripPath, poiName, diary, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class edi_poi_diary_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String tripPath;
			private String poiName;
			private String diary;

			public edi_poi_diary_call(String token, String tripPath, String poiName, String diary, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.tripPath = tripPath;
				this.poiName = poiName;
				this.diary = diary;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("edi_poi_diary", org.apache.thrift.protocol.TMessageType.CALL, 0));
				edi_poi_diary_args args = new edi_poi_diary_args();
				args.setToken(token);
				args.setTripPath(tripPath);
				args.setPoiName(poiName);
				args.setDiary(diary);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public boolean getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_edi_poi_diary();
			}
		}

		public void edi_poi_basicinformation(String token, String tripPath, String poiName, String content, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			edi_poi_basicinformation_call method_call = new edi_poi_basicinformation_call(token, tripPath, poiName, content, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class edi_poi_basicinformation_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String tripPath;
			private String poiName;
			private String content;

			public edi_poi_basicinformation_call(String token, String tripPath, String poiName, String content, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.tripPath = tripPath;
				this.poiName = poiName;
				this.content = content;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("edi_poi_basicinformation", org.apache.thrift.protocol.TMessageType.CALL, 0));
				edi_poi_basicinformation_args args = new edi_poi_basicinformation_args();
				args.setToken(token);
				args.setTripPath(tripPath);
				args.setPoiName(poiName);
				args.setContent(content);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public boolean getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_edi_poi_basicinformation();
			}
		}

		public void delete_trip(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			delete_trip_call method_call = new delete_trip_call(token, tripPath, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class delete_trip_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String tripPath;

			public delete_trip_call(String token, String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.tripPath = tripPath;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("delete_trip", org.apache.thrift.protocol.TMessageType.CALL, 0));
				delete_trip_args args = new delete_trip_args();
				args.setToken(token);
				args.setTripPath(tripPath);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public boolean getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_delete_trip();
			}
		}

		public void rename_poi_file(String token, String path, String newName, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			rename_poi_file_call method_call = new rename_poi_file_call(token, path, newName, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class rename_poi_file_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String path;
			private String newName;

			public rename_poi_file_call(String token, String path, String newName, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.path = path;
				this.newName = newName;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("rename_poi_file", org.apache.thrift.protocol.TMessageType.CALL, 0));
				rename_poi_file_args args = new rename_poi_file_args();
				args.setToken(token);
				args.setPath(path);
				args.setNewName(newName);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public boolean getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_rename_poi_file();
			}
		}

		public void rename_poi(String token, String tripPath, String poiName, String newPOIName, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			rename_poi_call method_call = new rename_poi_call(token, tripPath, poiName, newPOIName, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class rename_poi_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String tripPath;
			private String poiName;
			private String newPOIName;

			public rename_poi_call(String token, String tripPath, String poiName, String newPOIName, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.tripPath = tripPath;
				this.poiName = poiName;
				this.newPOIName = newPOIName;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("rename_poi", org.apache.thrift.protocol.TMessageType.CALL, 0));
				rename_poi_args args = new rename_poi_args();
				args.setToken(token);
				args.setTripPath(tripPath);
				args.setPoiName(poiName);
				args.setNewPOIName(newPOIName);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public boolean getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_rename_poi();
			}
		}

		public void rename_trip(String token, String trippath, String newTripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			rename_trip_call method_call = new rename_trip_call(token, trippath, newTripPath, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class rename_trip_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String trippath;
			private String newTripPath;

			public rename_trip_call(String token, String trippath, String newTripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.trippath = trippath;
				this.newTripPath = newTripPath;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("rename_trip", org.apache.thrift.protocol.TMessageType.CALL, 0));
				rename_trip_args args = new rename_trip_args();
				args.setToken(token);
				args.setTrippath(trippath);
				args.setNewTripPath(newTripPath);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public boolean getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_rename_trip();
			}
		}

		public void add_poi(String token, String tripPath, String poiName, String basicinformation, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			add_poi_call method_call = new add_poi_call(token, tripPath, poiName, basicinformation, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class add_poi_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String tripPath;
			private String poiName;
			private String basicinformation;

			public add_poi_call(String token, String tripPath, String poiName, String basicinformation, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.tripPath = tripPath;
				this.poiName = poiName;
				this.basicinformation = basicinformation;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("add_poi", org.apache.thrift.protocol.TMessageType.CALL, 0));
				add_poi_args args = new add_poi_args();
				args.setToken(token);
				args.setTripPath(tripPath);
				args.setPoiName(poiName);
				args.setBasicinformation(basicinformation);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public boolean getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_add_poi();
			}
		}

		public void toggle_public(String token, String tripPath, String option, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			toggle_public_call method_call = new toggle_public_call(token, tripPath, option, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class toggle_public_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String tripPath;
			private String option;

			public toggle_public_call(String token, String tripPath, String option, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.tripPath = tripPath;
				this.option = option;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("toggle_public", org.apache.thrift.protocol.TMessageType.CALL, 0));
				toggle_public_args args = new toggle_public_args();
				args.setToken(token);
				args.setTripPath(tripPath);
				args.setOption(option);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public boolean getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_toggle_public();
			}
		}

		public void add_like(String token, String tripPath, String who, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			add_like_call method_call = new add_like_call(token, tripPath, who, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class add_like_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String tripPath;
			private String who;

			public add_like_call(String token, String tripPath, String who, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.tripPath = tripPath;
				this.who = who;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("add_like", org.apache.thrift.protocol.TMessageType.CALL, 0));
				add_like_args args = new add_like_args();
				args.setToken(token);
				args.setTripPath(tripPath);
				args.setWho(who);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public boolean getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_add_like();
			}
		}

		public void delete_like(String token, String tripPath, String who, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			delete_like_call method_call = new delete_like_call(token, tripPath, who, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class delete_like_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String tripPath;
			private String who;

			public delete_like_call(String token, String tripPath, String who, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.tripPath = tripPath;
				this.who = who;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("delete_like", org.apache.thrift.protocol.TMessageType.CALL, 0));
				delete_like_args args = new delete_like_args();
				args.setToken(token);
				args.setTripPath(tripPath);
				args.setWho(who);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public boolean getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_delete_like();
			}
		}

		public void add_post(String token, String tripPath, String who, String content, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			add_post_call method_call = new add_post_call(token, tripPath, who, content, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class add_post_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String token;
			private String tripPath;
			private String who;
			private String content;

			public add_post_call(String token, String tripPath, String who, String content, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.token = token;
				this.tripPath = tripPath;
				this.who = who;
				this.content = content;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("add_post", org.apache.thrift.protocol.TMessageType.CALL, 0));
				add_post_args args = new add_post_args();
				args.setToken(token);
				args.setTripPath(tripPath);
				args.setWho(who);
				args.setContent(content);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public boolean getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_add_post();
			}
		}

		public void add_view(String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			add_view_call method_call = new add_view_call(tripPath, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class add_view_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String tripPath;

			public add_view_call(String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.tripPath = tripPath;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("add_view", org.apache.thrift.protocol.TMessageType.CALL, 0));
				add_view_args args = new add_view_args();
				args.setTripPath(tripPath);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public boolean getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_add_view();
			}
		}

		public void check_trip_public(String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler) throws org.apache.thrift.TException {
			checkReady();
			check_trip_public_call method_call = new check_trip_public_call(tripPath, resultHandler, this, ___protocolFactory, ___transport);
			this.___currentMethod = method_call;
			___manager.call(method_call);
		}

		public static class check_trip_public_call extends org.apache.thrift.async.TAsyncMethodCall {
			private String tripPath;

			public check_trip_public_call(String tripPath, org.apache.thrift.async.AsyncMethodCallback resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
				super(client, protocolFactory, transport, resultHandler, false);
				this.tripPath = tripPath;
			}

			public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
				prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("check_trip_public", org.apache.thrift.protocol.TMessageType.CALL, 0));
				check_trip_public_args args = new check_trip_public_args();
				args.setTripPath(tripPath);
				args.write(prot);
				prot.writeMessageEnd();
			}

			public boolean getResult() throws org.apache.thrift.TException {
				if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
					throw new IllegalStateException("Method call not finished!");
				}
				org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
				org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
				return (new Client(prot)).recv_check_trip_public();
			}
		}

	}

	public static class Processor<I extends Iface> extends org.apache.thrift.TBaseProcessor<I> implements org.apache.thrift.TProcessor {
		private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());

		public Processor(I iface) {
			super(iface, getProcessMap(new HashMap<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>>()));
		}

		protected Processor(I iface, Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {
			super(iface, getProcessMap(processMap));
		}

		private static <I extends Iface> Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> getProcessMap(Map<String, org.apache.thrift.ProcessFunction<I, ? extends org.apache.thrift.TBase>> processMap) {
			processMap.put("getTrips", new getTrips());
			processMap.put("getTrip", new getTrip());
			processMap.put("getPosts", new getPosts());
			processMap.put("getGpx", new getGpx());
			processMap.put("zipTrip", new zipTrip());
			processMap.put("edit_trip_note", new edit_trip_note());
			processMap.put("edi_poi_diary", new edi_poi_diary());
			processMap.put("edi_poi_basicinformation", new edi_poi_basicinformation());
			processMap.put("delete_trip", new delete_trip());
			processMap.put("rename_poi_file", new rename_poi_file());
			processMap.put("rename_poi", new rename_poi());
			processMap.put("rename_trip", new rename_trip());
			processMap.put("add_poi", new add_poi());
			processMap.put("toggle_public", new toggle_public());
			processMap.put("add_like", new add_like());
			processMap.put("delete_like", new delete_like());
			processMap.put("add_post", new add_post());
			processMap.put("add_view", new add_view());
			processMap.put("check_trip_public", new check_trip_public());
			return processMap;
		}

		public static class getTrips<I extends Iface> extends org.apache.thrift.ProcessFunction<I, getTrips_args> {
			public getTrips() {
				super("getTrips");
			}

			public getTrips_args getEmptyArgsInstance() {
				return new getTrips_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public getTrips_result getResult(I iface, getTrips_args args) throws org.apache.thrift.TException {
				getTrips_result result = new getTrips_result();
				result.success = iface.getTrips(args.token, args.isPublic, args.author, args.section);
				return result;
			}
		}

		public static class getTrip<I extends Iface> extends org.apache.thrift.ProcessFunction<I, getTrip_args> {
			public getTrip() {
				super("getTrip");
			}

			public getTrip_args getEmptyArgsInstance() {
				return new getTrip_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public getTrip_result getResult(I iface, getTrip_args args) throws org.apache.thrift.TException {
				getTrip_result result = new getTrip_result();
				result.success = iface.getTrip(args.token, args.tripPath);
				return result;
			}
		}

		public static class getPosts<I extends Iface> extends org.apache.thrift.ProcessFunction<I, getPosts_args> {
			public getPosts() {
				super("getPosts");
			}

			public getPosts_args getEmptyArgsInstance() {
				return new getPosts_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public getPosts_result getResult(I iface, getPosts_args args) throws org.apache.thrift.TException {
				getPosts_result result = new getPosts_result();
				result.success = iface.getPosts(args.token, args.tripPath);
				return result;
			}
		}

		public static class getGpx<I extends Iface> extends org.apache.thrift.ProcessFunction<I, getGpx_args> {
			public getGpx() {
				super("getGpx");
			}

			public getGpx_args getEmptyArgsInstance() {
				return new getGpx_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public getGpx_result getResult(I iface, getGpx_args args) throws org.apache.thrift.TException {
				getGpx_result result = new getGpx_result();
				result.success = iface.getGpx(args.token, args.tripPath);
				return result;
			}
		}

		public static class zipTrip<I extends Iface> extends org.apache.thrift.ProcessFunction<I, zipTrip_args> {
			public zipTrip() {
				super("zipTrip");
			}

			public zipTrip_args getEmptyArgsInstance() {
				return new zipTrip_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public zipTrip_result getResult(I iface, zipTrip_args args) throws org.apache.thrift.TException {
				zipTrip_result result = new zipTrip_result();
				result.success = iface.zipTrip(args.token, args.tripPath);
				return result;
			}
		}

		public static class edit_trip_note<I extends Iface> extends org.apache.thrift.ProcessFunction<I, edit_trip_note_args> {
			public edit_trip_note() {
				super("edit_trip_note");
			}

			public edit_trip_note_args getEmptyArgsInstance() {
				return new edit_trip_note_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public edit_trip_note_result getResult(I iface, edit_trip_note_args args) throws org.apache.thrift.TException {
				edit_trip_note_result result = new edit_trip_note_result();
				result.success = iface.edit_trip_note(args.token, args.tripPath, args.note);
				result.setSuccessIsSet(true);
				return result;
			}
		}

		public static class edi_poi_diary<I extends Iface> extends org.apache.thrift.ProcessFunction<I, edi_poi_diary_args> {
			public edi_poi_diary() {
				super("edi_poi_diary");
			}

			public edi_poi_diary_args getEmptyArgsInstance() {
				return new edi_poi_diary_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public edi_poi_diary_result getResult(I iface, edi_poi_diary_args args) throws org.apache.thrift.TException {
				edi_poi_diary_result result = new edi_poi_diary_result();
				result.success = iface.edi_poi_diary(args.token, args.tripPath, args.poiName, args.diary);
				result.setSuccessIsSet(true);
				return result;
			}
		}

		public static class edi_poi_basicinformation<I extends Iface> extends org.apache.thrift.ProcessFunction<I, edi_poi_basicinformation_args> {
			public edi_poi_basicinformation() {
				super("edi_poi_basicinformation");
			}

			public edi_poi_basicinformation_args getEmptyArgsInstance() {
				return new edi_poi_basicinformation_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public edi_poi_basicinformation_result getResult(I iface, edi_poi_basicinformation_args args) throws org.apache.thrift.TException {
				edi_poi_basicinformation_result result = new edi_poi_basicinformation_result();
				result.success = iface.edi_poi_basicinformation(args.token, args.tripPath, args.poiName, args.content);
				result.setSuccessIsSet(true);
				return result;
			}
		}

		public static class delete_trip<I extends Iface> extends org.apache.thrift.ProcessFunction<I, delete_trip_args> {
			public delete_trip() {
				super("delete_trip");
			}

			public delete_trip_args getEmptyArgsInstance() {
				return new delete_trip_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public delete_trip_result getResult(I iface, delete_trip_args args) throws org.apache.thrift.TException {
				delete_trip_result result = new delete_trip_result();
				result.success = iface.delete_trip(args.token, args.tripPath);
				result.setSuccessIsSet(true);
				return result;
			}
		}

		public static class rename_poi_file<I extends Iface> extends org.apache.thrift.ProcessFunction<I, rename_poi_file_args> {
			public rename_poi_file() {
				super("rename_poi_file");
			}

			public rename_poi_file_args getEmptyArgsInstance() {
				return new rename_poi_file_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public rename_poi_file_result getResult(I iface, rename_poi_file_args args) throws org.apache.thrift.TException {
				rename_poi_file_result result = new rename_poi_file_result();
				result.success = iface.rename_poi_file(args.token, args.path, args.newName);
				result.setSuccessIsSet(true);
				return result;
			}
		}

		public static class rename_poi<I extends Iface> extends org.apache.thrift.ProcessFunction<I, rename_poi_args> {
			public rename_poi() {
				super("rename_poi");
			}

			public rename_poi_args getEmptyArgsInstance() {
				return new rename_poi_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public rename_poi_result getResult(I iface, rename_poi_args args) throws org.apache.thrift.TException {
				rename_poi_result result = new rename_poi_result();
				result.success = iface.rename_poi(args.token, args.tripPath, args.poiName, args.newPOIName);
				result.setSuccessIsSet(true);
				return result;
			}
		}

		public static class rename_trip<I extends Iface> extends org.apache.thrift.ProcessFunction<I, rename_trip_args> {
			public rename_trip() {
				super("rename_trip");
			}

			public rename_trip_args getEmptyArgsInstance() {
				return new rename_trip_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public rename_trip_result getResult(I iface, rename_trip_args args) throws org.apache.thrift.TException {
				rename_trip_result result = new rename_trip_result();
				result.success = iface.rename_trip(args.token, args.trippath, args.newTripPath);
				result.setSuccessIsSet(true);
				return result;
			}
		}

		public static class add_poi<I extends Iface> extends org.apache.thrift.ProcessFunction<I, add_poi_args> {
			public add_poi() {
				super("add_poi");
			}

			public add_poi_args getEmptyArgsInstance() {
				return new add_poi_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public add_poi_result getResult(I iface, add_poi_args args) throws org.apache.thrift.TException {
				add_poi_result result = new add_poi_result();
				result.success = iface.add_poi(args.token, args.tripPath, args.poiName, args.basicinformation);
				result.setSuccessIsSet(true);
				return result;
			}
		}

		public static class toggle_public<I extends Iface> extends org.apache.thrift.ProcessFunction<I, toggle_public_args> {
			public toggle_public() {
				super("toggle_public");
			}

			public toggle_public_args getEmptyArgsInstance() {
				return new toggle_public_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public toggle_public_result getResult(I iface, toggle_public_args args) throws org.apache.thrift.TException {
				toggle_public_result result = new toggle_public_result();
				result.success = iface.toggle_public(args.token, args.tripPath, args.option);
				result.setSuccessIsSet(true);
				return result;
			}
		}

		public static class add_like<I extends Iface> extends org.apache.thrift.ProcessFunction<I, add_like_args> {
			public add_like() {
				super("add_like");
			}

			public add_like_args getEmptyArgsInstance() {
				return new add_like_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public add_like_result getResult(I iface, add_like_args args) throws org.apache.thrift.TException {
				add_like_result result = new add_like_result();
				result.success = iface.add_like(args.token, args.tripPath, args.who);
				result.setSuccessIsSet(true);
				return result;
			}
		}

		public static class delete_like<I extends Iface> extends org.apache.thrift.ProcessFunction<I, delete_like_args> {
			public delete_like() {
				super("delete_like");
			}

			public delete_like_args getEmptyArgsInstance() {
				return new delete_like_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public delete_like_result getResult(I iface, delete_like_args args) throws org.apache.thrift.TException {
				delete_like_result result = new delete_like_result();
				result.success = iface.delete_like(args.token, args.tripPath, args.who);
				result.setSuccessIsSet(true);
				return result;
			}
		}

		public static class add_post<I extends Iface> extends org.apache.thrift.ProcessFunction<I, add_post_args> {
			public add_post() {
				super("add_post");
			}

			public add_post_args getEmptyArgsInstance() {
				return new add_post_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public add_post_result getResult(I iface, add_post_args args) throws org.apache.thrift.TException {
				add_post_result result = new add_post_result();
				result.success = iface.add_post(args.token, args.tripPath, args.who, args.content);
				result.setSuccessIsSet(true);
				return result;
			}
		}

		public static class add_view<I extends Iface> extends org.apache.thrift.ProcessFunction<I, add_view_args> {
			public add_view() {
				super("add_view");
			}

			public add_view_args getEmptyArgsInstance() {
				return new add_view_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public add_view_result getResult(I iface, add_view_args args) throws org.apache.thrift.TException {
				add_view_result result = new add_view_result();
				result.success = iface.add_view(args.tripPath);
				result.setSuccessIsSet(true);
				return result;
			}
		}

		public static class check_trip_public<I extends Iface> extends org.apache.thrift.ProcessFunction<I, check_trip_public_args> {
			public check_trip_public() {
				super("check_trip_public");
			}

			public check_trip_public_args getEmptyArgsInstance() {
				return new check_trip_public_args();
			}

			protected boolean isOneway() {
				return false;
			}

			public check_trip_public_result getResult(I iface, check_trip_public_args args) throws org.apache.thrift.TException {
				check_trip_public_result result = new check_trip_public_result();
				result.success = iface.check_trip_public(args.tripPath);
				result.setSuccessIsSet(true);
				return result;
			}
		}

	}

	public static class AsyncProcessor<I extends AsyncIface> extends org.apache.thrift.TBaseAsyncProcessor<I> {
		private static final Logger LOGGER = LoggerFactory.getLogger(AsyncProcessor.class.getName());

		public AsyncProcessor(I iface) {
			super(iface, getProcessMap(new HashMap<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>>()));
		}

		protected AsyncProcessor(I iface, Map<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>> processMap) {
			super(iface, getProcessMap(processMap));
		}

		private static <I extends AsyncIface> Map<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>> getProcessMap(Map<String, org.apache.thrift.AsyncProcessFunction<I, ? extends org.apache.thrift.TBase, ?>> processMap) {
			processMap.put("getTrips", new getTrips());
			processMap.put("getTrip", new getTrip());
			processMap.put("getPosts", new getPosts());
			processMap.put("getGpx", new getGpx());
			processMap.put("zipTrip", new zipTrip());
			processMap.put("edit_trip_note", new edit_trip_note());
			processMap.put("edi_poi_diary", new edi_poi_diary());
			processMap.put("edi_poi_basicinformation", new edi_poi_basicinformation());
			processMap.put("delete_trip", new delete_trip());
			processMap.put("rename_poi_file", new rename_poi_file());
			processMap.put("rename_poi", new rename_poi());
			processMap.put("rename_trip", new rename_trip());
			processMap.put("add_poi", new add_poi());
			processMap.put("toggle_public", new toggle_public());
			processMap.put("add_like", new add_like());
			processMap.put("delete_like", new delete_like());
			processMap.put("add_post", new add_post());
			processMap.put("add_view", new add_view());
			processMap.put("check_trip_public", new check_trip_public());
			return processMap;
		}

		public static class getTrips<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, getTrips_args, List<Trip>> {
			public getTrips() {
				super("getTrips");
			}

			public getTrips_args getEmptyArgsInstance() {
				return new getTrips_args();
			}

			public AsyncMethodCallback<List<Trip>> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<List<Trip>>() {
					public void onComplete(List<Trip> o) {
						getTrips_result result = new getTrips_result();
						result.success = o;
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						getTrips_result result = new getTrips_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, getTrips_args args, org.apache.thrift.async.AsyncMethodCallback<List<Trip>> resultHandler) throws TException {
				iface.getTrips(args.token, args.isPublic, args.author, args.section, resultHandler);
			}
		}

		public static class getTrip<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, getTrip_args, Trip> {
			public getTrip() {
				super("getTrip");
			}

			public getTrip_args getEmptyArgsInstance() {
				return new getTrip_args();
			}

			public AsyncMethodCallback<Trip> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Trip>() {
					public void onComplete(Trip o) {
						getTrip_result result = new getTrip_result();
						result.success = o;
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						getTrip_result result = new getTrip_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, getTrip_args args, org.apache.thrift.async.AsyncMethodCallback<Trip> resultHandler) throws TException {
				iface.getTrip(args.token, args.tripPath, resultHandler);
			}
		}

		public static class getPosts<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, getPosts_args, List<Post>> {
			public getPosts() {
				super("getPosts");
			}

			public getPosts_args getEmptyArgsInstance() {
				return new getPosts_args();
			}

			public AsyncMethodCallback<List<Post>> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<List<Post>>() {
					public void onComplete(List<Post> o) {
						getPosts_result result = new getPosts_result();
						result.success = o;
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						getPosts_result result = new getPosts_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, getPosts_args args, org.apache.thrift.async.AsyncMethodCallback<List<Post>> resultHandler) throws TException {
				iface.getPosts(args.token, args.tripPath, resultHandler);
			}
		}

		public static class getGpx<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, getGpx_args, String> {
			public getGpx() {
				super("getGpx");
			}

			public getGpx_args getEmptyArgsInstance() {
				return new getGpx_args();
			}

			public AsyncMethodCallback<String> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<String>() {
					public void onComplete(String o) {
						getGpx_result result = new getGpx_result();
						result.success = o;
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						getGpx_result result = new getGpx_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, getGpx_args args, org.apache.thrift.async.AsyncMethodCallback<String> resultHandler) throws TException {
				iface.getGpx(args.token, args.tripPath, resultHandler);
			}
		}

		public static class zipTrip<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, zipTrip_args, String> {
			public zipTrip() {
				super("zipTrip");
			}

			public zipTrip_args getEmptyArgsInstance() {
				return new zipTrip_args();
			}

			public AsyncMethodCallback<String> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<String>() {
					public void onComplete(String o) {
						zipTrip_result result = new zipTrip_result();
						result.success = o;
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						zipTrip_result result = new zipTrip_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, zipTrip_args args, org.apache.thrift.async.AsyncMethodCallback<String> resultHandler) throws TException {
				iface.zipTrip(args.token, args.tripPath, resultHandler);
			}
		}

		public static class edit_trip_note<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, edit_trip_note_args, Boolean> {
			public edit_trip_note() {
				super("edit_trip_note");
			}

			public edit_trip_note_args getEmptyArgsInstance() {
				return new edit_trip_note_args();
			}

			public AsyncMethodCallback<Boolean> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Boolean>() {
					public void onComplete(Boolean o) {
						edit_trip_note_result result = new edit_trip_note_result();
						result.success = o;
						result.setSuccessIsSet(true);
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						edit_trip_note_result result = new edit_trip_note_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, edit_trip_note_args args, org.apache.thrift.async.AsyncMethodCallback<Boolean> resultHandler) throws TException {
				iface.edit_trip_note(args.token, args.tripPath, args.note, resultHandler);
			}
		}

		public static class edi_poi_diary<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, edi_poi_diary_args, Boolean> {
			public edi_poi_diary() {
				super("edi_poi_diary");
			}

			public edi_poi_diary_args getEmptyArgsInstance() {
				return new edi_poi_diary_args();
			}

			public AsyncMethodCallback<Boolean> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Boolean>() {
					public void onComplete(Boolean o) {
						edi_poi_diary_result result = new edi_poi_diary_result();
						result.success = o;
						result.setSuccessIsSet(true);
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						edi_poi_diary_result result = new edi_poi_diary_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, edi_poi_diary_args args, org.apache.thrift.async.AsyncMethodCallback<Boolean> resultHandler) throws TException {
				iface.edi_poi_diary(args.token, args.tripPath, args.poiName, args.diary, resultHandler);
			}
		}

		public static class edi_poi_basicinformation<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, edi_poi_basicinformation_args, Boolean> {
			public edi_poi_basicinformation() {
				super("edi_poi_basicinformation");
			}

			public edi_poi_basicinformation_args getEmptyArgsInstance() {
				return new edi_poi_basicinformation_args();
			}

			public AsyncMethodCallback<Boolean> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Boolean>() {
					public void onComplete(Boolean o) {
						edi_poi_basicinformation_result result = new edi_poi_basicinformation_result();
						result.success = o;
						result.setSuccessIsSet(true);
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						edi_poi_basicinformation_result result = new edi_poi_basicinformation_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, edi_poi_basicinformation_args args, org.apache.thrift.async.AsyncMethodCallback<Boolean> resultHandler) throws TException {
				iface.edi_poi_basicinformation(args.token, args.tripPath, args.poiName, args.content, resultHandler);
			}
		}

		public static class delete_trip<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, delete_trip_args, Boolean> {
			public delete_trip() {
				super("delete_trip");
			}

			public delete_trip_args getEmptyArgsInstance() {
				return new delete_trip_args();
			}

			public AsyncMethodCallback<Boolean> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Boolean>() {
					public void onComplete(Boolean o) {
						delete_trip_result result = new delete_trip_result();
						result.success = o;
						result.setSuccessIsSet(true);
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						delete_trip_result result = new delete_trip_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, delete_trip_args args, org.apache.thrift.async.AsyncMethodCallback<Boolean> resultHandler) throws TException {
				iface.delete_trip(args.token, args.tripPath, resultHandler);
			}
		}

		public static class rename_poi_file<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, rename_poi_file_args, Boolean> {
			public rename_poi_file() {
				super("rename_poi_file");
			}

			public rename_poi_file_args getEmptyArgsInstance() {
				return new rename_poi_file_args();
			}

			public AsyncMethodCallback<Boolean> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Boolean>() {
					public void onComplete(Boolean o) {
						rename_poi_file_result result = new rename_poi_file_result();
						result.success = o;
						result.setSuccessIsSet(true);
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						rename_poi_file_result result = new rename_poi_file_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, rename_poi_file_args args, org.apache.thrift.async.AsyncMethodCallback<Boolean> resultHandler) throws TException {
				iface.rename_poi_file(args.token, args.path, args.newName, resultHandler);
			}
		}

		public static class rename_poi<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, rename_poi_args, Boolean> {
			public rename_poi() {
				super("rename_poi");
			}

			public rename_poi_args getEmptyArgsInstance() {
				return new rename_poi_args();
			}

			public AsyncMethodCallback<Boolean> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Boolean>() {
					public void onComplete(Boolean o) {
						rename_poi_result result = new rename_poi_result();
						result.success = o;
						result.setSuccessIsSet(true);
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						rename_poi_result result = new rename_poi_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, rename_poi_args args, org.apache.thrift.async.AsyncMethodCallback<Boolean> resultHandler) throws TException {
				iface.rename_poi(args.token, args.tripPath, args.poiName, args.newPOIName, resultHandler);
			}
		}

		public static class rename_trip<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, rename_trip_args, Boolean> {
			public rename_trip() {
				super("rename_trip");
			}

			public rename_trip_args getEmptyArgsInstance() {
				return new rename_trip_args();
			}

			public AsyncMethodCallback<Boolean> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Boolean>() {
					public void onComplete(Boolean o) {
						rename_trip_result result = new rename_trip_result();
						result.success = o;
						result.setSuccessIsSet(true);
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						rename_trip_result result = new rename_trip_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, rename_trip_args args, org.apache.thrift.async.AsyncMethodCallback<Boolean> resultHandler) throws TException {
				iface.rename_trip(args.token, args.trippath, args.newTripPath, resultHandler);
			}
		}

		public static class add_poi<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, add_poi_args, Boolean> {
			public add_poi() {
				super("add_poi");
			}

			public add_poi_args getEmptyArgsInstance() {
				return new add_poi_args();
			}

			public AsyncMethodCallback<Boolean> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Boolean>() {
					public void onComplete(Boolean o) {
						add_poi_result result = new add_poi_result();
						result.success = o;
						result.setSuccessIsSet(true);
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						add_poi_result result = new add_poi_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, add_poi_args args, org.apache.thrift.async.AsyncMethodCallback<Boolean> resultHandler) throws TException {
				iface.add_poi(args.token, args.tripPath, args.poiName, args.basicinformation, resultHandler);
			}
		}

		public static class toggle_public<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, toggle_public_args, Boolean> {
			public toggle_public() {
				super("toggle_public");
			}

			public toggle_public_args getEmptyArgsInstance() {
				return new toggle_public_args();
			}

			public AsyncMethodCallback<Boolean> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Boolean>() {
					public void onComplete(Boolean o) {
						toggle_public_result result = new toggle_public_result();
						result.success = o;
						result.setSuccessIsSet(true);
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						toggle_public_result result = new toggle_public_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, toggle_public_args args, org.apache.thrift.async.AsyncMethodCallback<Boolean> resultHandler) throws TException {
				iface.toggle_public(args.token, args.tripPath, args.option, resultHandler);
			}
		}

		public static class add_like<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, add_like_args, Boolean> {
			public add_like() {
				super("add_like");
			}

			public add_like_args getEmptyArgsInstance() {
				return new add_like_args();
			}

			public AsyncMethodCallback<Boolean> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Boolean>() {
					public void onComplete(Boolean o) {
						add_like_result result = new add_like_result();
						result.success = o;
						result.setSuccessIsSet(true);
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						add_like_result result = new add_like_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, add_like_args args, org.apache.thrift.async.AsyncMethodCallback<Boolean> resultHandler) throws TException {
				iface.add_like(args.token, args.tripPath, args.who, resultHandler);
			}
		}

		public static class delete_like<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, delete_like_args, Boolean> {
			public delete_like() {
				super("delete_like");
			}

			public delete_like_args getEmptyArgsInstance() {
				return new delete_like_args();
			}

			public AsyncMethodCallback<Boolean> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Boolean>() {
					public void onComplete(Boolean o) {
						delete_like_result result = new delete_like_result();
						result.success = o;
						result.setSuccessIsSet(true);
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						delete_like_result result = new delete_like_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, delete_like_args args, org.apache.thrift.async.AsyncMethodCallback<Boolean> resultHandler) throws TException {
				iface.delete_like(args.token, args.tripPath, args.who, resultHandler);
			}
		}

		public static class add_post<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, add_post_args, Boolean> {
			public add_post() {
				super("add_post");
			}

			public add_post_args getEmptyArgsInstance() {
				return new add_post_args();
			}

			public AsyncMethodCallback<Boolean> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Boolean>() {
					public void onComplete(Boolean o) {
						add_post_result result = new add_post_result();
						result.success = o;
						result.setSuccessIsSet(true);
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						add_post_result result = new add_post_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, add_post_args args, org.apache.thrift.async.AsyncMethodCallback<Boolean> resultHandler) throws TException {
				iface.add_post(args.token, args.tripPath, args.who, args.content, resultHandler);
			}
		}

		public static class add_view<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, add_view_args, Boolean> {
			public add_view() {
				super("add_view");
			}

			public add_view_args getEmptyArgsInstance() {
				return new add_view_args();
			}

			public AsyncMethodCallback<Boolean> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Boolean>() {
					public void onComplete(Boolean o) {
						add_view_result result = new add_view_result();
						result.success = o;
						result.setSuccessIsSet(true);
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						add_view_result result = new add_view_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, add_view_args args, org.apache.thrift.async.AsyncMethodCallback<Boolean> resultHandler) throws TException {
				iface.add_view(args.tripPath, resultHandler);
			}
		}

		public static class check_trip_public<I extends AsyncIface> extends org.apache.thrift.AsyncProcessFunction<I, check_trip_public_args, Boolean> {
			public check_trip_public() {
				super("check_trip_public");
			}

			public check_trip_public_args getEmptyArgsInstance() {
				return new check_trip_public_args();
			}

			public AsyncMethodCallback<Boolean> getResultHandler(final AsyncFrameBuffer fb, final int seqid) {
				final org.apache.thrift.AsyncProcessFunction fcall = this;
				return new AsyncMethodCallback<Boolean>() {
					public void onComplete(Boolean o) {
						check_trip_public_result result = new check_trip_public_result();
						result.success = o;
						result.setSuccessIsSet(true);
						try {
							fcall.sendResponse(fb, result, org.apache.thrift.protocol.TMessageType.REPLY, seqid);
							return;
						} catch (Exception e) {
							LOGGER.error("Exception writing to internal frame buffer", e);
						}
						fb.close();
					}

					public void onError(Exception e) {
						byte msgType = org.apache.thrift.protocol.TMessageType.REPLY;
						org.apache.thrift.TBase msg;
						check_trip_public_result result = new check_trip_public_result();
						{
							msgType = org.apache.thrift.protocol.TMessageType.EXCEPTION;
							msg = (org.apache.thrift.TBase) new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.INTERNAL_ERROR, e.getMessage());
						}
						try {
							fcall.sendResponse(fb, msg, msgType, seqid);
							return;
						} catch (Exception ex) {
							LOGGER.error("Exception writing to internal frame buffer", ex);
						}
						fb.close();
					}
				};
			}

			protected boolean isOneway() {
				return false;
			}

			public void start(I iface, check_trip_public_args args, org.apache.thrift.async.AsyncMethodCallback<Boolean> resultHandler) throws TException {
				iface.check_trip_public(args.tripPath, resultHandler);
			}
		}

	}

	public static class getTrips_args implements org.apache.thrift.TBase<getTrips_args, getTrips_args._Fields>, java.io.Serializable, Cloneable, Comparable<getTrips_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("getTrips_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField IS_PUBLIC_FIELD_DESC = new org.apache.thrift.protocol.TField("isPublic", org.apache.thrift.protocol.TType.BOOL, (short) 2);
		private static final org.apache.thrift.protocol.TField AUTHOR_FIELD_DESC = new org.apache.thrift.protocol.TField("author", org.apache.thrift.protocol.TType.STRING, (short) 3);
		private static final org.apache.thrift.protocol.TField SECTION_FIELD_DESC = new org.apache.thrift.protocol.TField("section", org.apache.thrift.protocol.TType.I32, (short) 4);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new getTrips_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new getTrips_argsTupleSchemeFactory());
		}

		public String token; // required
		public boolean isPublic; // required
		public String author; // required
		public int section; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), IS_PUBLIC((short) 2, "isPublic"), AUTHOR((short) 3, "author"), SECTION((short) 4, "section");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // IS_PUBLIC
					return IS_PUBLIC;
				case 3: // AUTHOR
					return AUTHOR;
				case 4: // SECTION
					return SECTION;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __ISPUBLIC_ISSET_ID = 0;
		private static final int __SECTION_ISSET_ID = 1;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.IS_PUBLIC, new org.apache.thrift.meta_data.FieldMetaData("isPublic", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			tmpMap.put(_Fields.AUTHOR, new org.apache.thrift.meta_data.FieldMetaData("author", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.SECTION, new org.apache.thrift.meta_data.FieldMetaData("section", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(getTrips_args.class, metaDataMap);
		}

		public getTrips_args() {
		}

		public getTrips_args(String token, boolean isPublic, String author, int section) {
			this();
			this.token = token;
			this.isPublic = isPublic;
			setIsPublicIsSet(true);
			this.author = author;
			this.section = section;
			setSectionIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public getTrips_args(getTrips_args other) {
			__isset_bitfield = other.__isset_bitfield;
			if (other.isSetToken()) {
				this.token = other.token;
			}
			this.isPublic = other.isPublic;
			if (other.isSetAuthor()) {
				this.author = other.author;
			}
			this.section = other.section;
		}

		public getTrips_args deepCopy() {
			return new getTrips_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			setIsPublicIsSet(false);
			this.isPublic = false;
			this.author = null;
			setSectionIsSet(false);
			this.section = 0;
		}

		public String getToken() {
			return this.token;
		}

		public getTrips_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public boolean isIsPublic() {
			return this.isPublic;
		}

		public getTrips_args setIsPublic(boolean isPublic) {
			this.isPublic = isPublic;
			setIsPublicIsSet(true);
			return this;
		}

		public void unsetIsPublic() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ISPUBLIC_ISSET_ID);
		}

		/**
		 * Returns true if field isPublic is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetIsPublic() {
			return EncodingUtils.testBit(__isset_bitfield, __ISPUBLIC_ISSET_ID);
		}

		public void setIsPublicIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ISPUBLIC_ISSET_ID, value);
		}

		public String getAuthor() {
			return this.author;
		}

		public getTrips_args setAuthor(String author) {
			this.author = author;
			return this;
		}

		public void unsetAuthor() {
			this.author = null;
		}

		/**
		 * Returns true if field author is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetAuthor() {
			return this.author != null;
		}

		public void setAuthorIsSet(boolean value) {
			if (!value) {
				this.author = null;
			}
		}

		public int getSection() {
			return this.section;
		}

		public getTrips_args setSection(int section) {
			this.section = section;
			setSectionIsSet(true);
			return this;
		}

		public void unsetSection() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SECTION_ISSET_ID);
		}

		/**
		 * Returns true if field section is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSection() {
			return EncodingUtils.testBit(__isset_bitfield, __SECTION_ISSET_ID);
		}

		public void setSectionIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SECTION_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case IS_PUBLIC:
				if (value == null) {
					unsetIsPublic();
				} else {
					setIsPublic((Boolean) value);
				}
				break;

			case AUTHOR:
				if (value == null) {
					unsetAuthor();
				} else {
					setAuthor((String) value);
				}
				break;

			case SECTION:
				if (value == null) {
					unsetSection();
				} else {
					setSection((Integer) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case IS_PUBLIC:
				return Boolean.valueOf(isIsPublic());

			case AUTHOR:
				return getAuthor();

			case SECTION:
				return Integer.valueOf(getSection());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case IS_PUBLIC:
				return isSetIsPublic();
			case AUTHOR:
				return isSetAuthor();
			case SECTION:
				return isSetSection();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof getTrips_args)
				return this.equals((getTrips_args) that);
			return false;
		}

		public boolean equals(getTrips_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_isPublic = true;
			boolean that_present_isPublic = true;
			if (this_present_isPublic || that_present_isPublic) {
				if (!(this_present_isPublic && that_present_isPublic))
					return false;
				if (this.isPublic != that.isPublic)
					return false;
			}

			boolean this_present_author = true && this.isSetAuthor();
			boolean that_present_author = true && that.isSetAuthor();
			if (this_present_author || that_present_author) {
				if (!(this_present_author && that_present_author))
					return false;
				if (!this.author.equals(that.author))
					return false;
			}

			boolean this_present_section = true;
			boolean that_present_section = true;
			if (this_present_section || that_present_section) {
				if (!(this_present_section && that_present_section))
					return false;
				if (this.section != that.section)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(getTrips_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetIsPublic()).compareTo(other.isSetIsPublic());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetIsPublic()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.isPublic, other.isPublic);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetAuthor()).compareTo(other.isSetAuthor());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetAuthor()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.author, other.author);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetSection()).compareTo(other.isSetSection());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSection()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.section, other.section);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("getTrips_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("isPublic:");
			sb.append(this.isPublic);
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("author:");
			if (this.author == null) {
				sb.append("null");
			} else {
				sb.append(this.author);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("section:");
			sb.append(this.section);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class getTrips_argsStandardSchemeFactory implements SchemeFactory {
			public getTrips_argsStandardScheme getScheme() {
				return new getTrips_argsStandardScheme();
			}
		}

		private static class getTrips_argsStandardScheme extends StandardScheme<getTrips_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, getTrips_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // IS_PUBLIC
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.isPublic = iprot.readBool();
							struct.setIsPublicIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 3: // AUTHOR
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.author = iprot.readString();
							struct.setAuthorIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 4: // SECTION
						if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
							struct.section = iprot.readI32();
							struct.setSectionIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, getTrips_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldBegin(IS_PUBLIC_FIELD_DESC);
				oprot.writeBool(struct.isPublic);
				oprot.writeFieldEnd();
				if (struct.author != null) {
					oprot.writeFieldBegin(AUTHOR_FIELD_DESC);
					oprot.writeString(struct.author);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldBegin(SECTION_FIELD_DESC);
				oprot.writeI32(struct.section);
				oprot.writeFieldEnd();
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class getTrips_argsTupleSchemeFactory implements SchemeFactory {
			public getTrips_argsTupleScheme getScheme() {
				return new getTrips_argsTupleScheme();
			}
		}

		private static class getTrips_argsTupleScheme extends TupleScheme<getTrips_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, getTrips_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetIsPublic()) {
					optionals.set(1);
				}
				if (struct.isSetAuthor()) {
					optionals.set(2);
				}
				if (struct.isSetSection()) {
					optionals.set(3);
				}
				oprot.writeBitSet(optionals, 4);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetIsPublic()) {
					oprot.writeBool(struct.isPublic);
				}
				if (struct.isSetAuthor()) {
					oprot.writeString(struct.author);
				}
				if (struct.isSetSection()) {
					oprot.writeI32(struct.section);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, getTrips_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(4);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.isPublic = iprot.readBool();
					struct.setIsPublicIsSet(true);
				}
				if (incoming.get(2)) {
					struct.author = iprot.readString();
					struct.setAuthorIsSet(true);
				}
				if (incoming.get(3)) {
					struct.section = iprot.readI32();
					struct.setSectionIsSet(true);
				}
			}
		}

	}

	public static class getTrips_result implements org.apache.thrift.TBase<getTrips_result, getTrips_result._Fields>, java.io.Serializable, Cloneable, Comparable<getTrips_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("getTrips_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.LIST, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new getTrips_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new getTrips_resultTupleSchemeFactory());
		}

		public List<Trip> success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Trip.class))));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(getTrips_result.class, metaDataMap);
		}

		public getTrips_result() {
		}

		public getTrips_result(List<Trip> success) {
			this();
			this.success = success;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public getTrips_result(getTrips_result other) {
			if (other.isSetSuccess()) {
				List<Trip> __this__success = new ArrayList<Trip>(other.success.size());
				for (Trip other_element : other.success) {
					__this__success.add(new Trip(other_element));
				}
				this.success = __this__success;
			}
		}

		public getTrips_result deepCopy() {
			return new getTrips_result(this);
		}

		@Override
		public void clear() {
			this.success = null;
		}

		public int getSuccessSize() {
			return (this.success == null) ? 0 : this.success.size();
		}

		public java.util.Iterator<Trip> getSuccessIterator() {
			return (this.success == null) ? null : this.success.iterator();
		}

		public void addToSuccess(Trip elem) {
			if (this.success == null) {
				this.success = new ArrayList<Trip>();
			}
			this.success.add(elem);
		}

		public List<Trip> getSuccess() {
			return this.success;
		}

		public getTrips_result setSuccess(List<Trip> success) {
			this.success = success;
			return this;
		}

		public void unsetSuccess() {
			this.success = null;
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return this.success != null;
		}

		public void setSuccessIsSet(boolean value) {
			if (!value) {
				this.success = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((List<Trip>) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return getSuccess();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof getTrips_result)
				return this.equals((getTrips_result) that);
			return false;
		}

		public boolean equals(getTrips_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true && this.isSetSuccess();
			boolean that_present_success = true && that.isSetSuccess();
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (!this.success.equals(that.success))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(getTrips_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("getTrips_result(");
			boolean first = true;

			sb.append("success:");
			if (this.success == null) {
				sb.append("null");
			} else {
				sb.append(this.success);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class getTrips_resultStandardSchemeFactory implements SchemeFactory {
			public getTrips_resultStandardScheme getScheme() {
				return new getTrips_resultStandardScheme();
			}
		}

		private static class getTrips_resultStandardScheme extends StandardScheme<getTrips_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, getTrips_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
							{
								org.apache.thrift.protocol.TList _list32 = iprot.readListBegin();
								struct.success = new ArrayList<Trip>(_list32.size);
								for (int _i33 = 0; _i33 < _list32.size; ++_i33) {
									Trip _elem34;
									_elem34 = new Trip();
									_elem34.read(iprot);
									struct.success.add(_elem34);
								}
								iprot.readListEnd();
							}
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, getTrips_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.success != null) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					{
						oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.success.size()));
						for (Trip _iter35 : struct.success) {
							_iter35.write(oprot);
						}
						oprot.writeListEnd();
					}
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class getTrips_resultTupleSchemeFactory implements SchemeFactory {
			public getTrips_resultTupleScheme getScheme() {
				return new getTrips_resultTupleScheme();
			}
		}

		private static class getTrips_resultTupleScheme extends TupleScheme<getTrips_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, getTrips_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					{
						oprot.writeI32(struct.success.size());
						for (Trip _iter36 : struct.success) {
							_iter36.write(oprot);
						}
					}
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, getTrips_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					{
						org.apache.thrift.protocol.TList _list37 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
						struct.success = new ArrayList<Trip>(_list37.size);
						for (int _i38 = 0; _i38 < _list37.size; ++_i38) {
							Trip _elem39;
							_elem39 = new Trip();
							_elem39.read(iprot);
							struct.success.add(_elem39);
						}
					}
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class getTrip_args implements org.apache.thrift.TBase<getTrip_args, getTrip_args._Fields>, java.io.Serializable, Cloneable, Comparable<getTrip_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("getTrip_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 2);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new getTrip_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new getTrip_argsTupleSchemeFactory());
		}

		public String token; // required
		public String tripPath; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIP_PATH((short) 2, "tripPath");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIP_PATH
					return TRIP_PATH;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(getTrip_args.class, metaDataMap);
		}

		public getTrip_args() {
		}

		public getTrip_args(String token, String tripPath) {
			this();
			this.token = token;
			this.tripPath = tripPath;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public getTrip_args(getTrip_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
		}

		public getTrip_args deepCopy() {
			return new getTrip_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.tripPath = null;
		}

		public String getToken() {
			return this.token;
		}

		public getTrip_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public getTrip_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIP_PATH:
				return getTripPath();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIP_PATH:
				return isSetTripPath();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof getTrip_args)
				return this.equals((getTrip_args) that);
			return false;
		}

		public boolean equals(getTrip_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(getTrip_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("getTrip_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class getTrip_argsStandardSchemeFactory implements SchemeFactory {
			public getTrip_argsStandardScheme getScheme() {
				return new getTrip_argsStandardScheme();
			}
		}

		private static class getTrip_argsStandardScheme extends StandardScheme<getTrip_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, getTrip_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, getTrip_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class getTrip_argsTupleSchemeFactory implements SchemeFactory {
			public getTrip_argsTupleScheme getScheme() {
				return new getTrip_argsTupleScheme();
			}
		}

		private static class getTrip_argsTupleScheme extends TupleScheme<getTrip_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, getTrip_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTripPath()) {
					optionals.set(1);
				}
				oprot.writeBitSet(optionals, 2);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, getTrip_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(2);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
			}
		}

	}

	public static class getTrip_result implements org.apache.thrift.TBase<getTrip_result, getTrip_result._Fields>, java.io.Serializable, Cloneable, Comparable<getTrip_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("getTrip_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.STRUCT, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new getTrip_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new getTrip_resultTupleSchemeFactory());
		}

		public Trip success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Trip.class)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(getTrip_result.class, metaDataMap);
		}

		public getTrip_result() {
		}

		public getTrip_result(Trip success) {
			this();
			this.success = success;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public getTrip_result(getTrip_result other) {
			if (other.isSetSuccess()) {
				this.success = new Trip(other.success);
			}
		}

		public getTrip_result deepCopy() {
			return new getTrip_result(this);
		}

		@Override
		public void clear() {
			this.success = null;
		}

		public Trip getSuccess() {
			return this.success;
		}

		public getTrip_result setSuccess(Trip success) {
			this.success = success;
			return this;
		}

		public void unsetSuccess() {
			this.success = null;
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return this.success != null;
		}

		public void setSuccessIsSet(boolean value) {
			if (!value) {
				this.success = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Trip) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return getSuccess();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof getTrip_result)
				return this.equals((getTrip_result) that);
			return false;
		}

		public boolean equals(getTrip_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true && this.isSetSuccess();
			boolean that_present_success = true && that.isSetSuccess();
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (!this.success.equals(that.success))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(getTrip_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("getTrip_result(");
			boolean first = true;

			sb.append("success:");
			if (this.success == null) {
				sb.append("null");
			} else {
				sb.append(this.success);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
			if (success != null) {
				success.validate();
			}
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class getTrip_resultStandardSchemeFactory implements SchemeFactory {
			public getTrip_resultStandardScheme getScheme() {
				return new getTrip_resultStandardScheme();
			}
		}

		private static class getTrip_resultStandardScheme extends StandardScheme<getTrip_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, getTrip_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
							struct.success = new Trip();
							struct.success.read(iprot);
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, getTrip_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.success != null) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					struct.success.write(oprot);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class getTrip_resultTupleSchemeFactory implements SchemeFactory {
			public getTrip_resultTupleScheme getScheme() {
				return new getTrip_resultTupleScheme();
			}
		}

		private static class getTrip_resultTupleScheme extends TupleScheme<getTrip_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, getTrip_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					struct.success.write(oprot);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, getTrip_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = new Trip();
					struct.success.read(iprot);
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class getPosts_args implements org.apache.thrift.TBase<getPosts_args, getPosts_args._Fields>, java.io.Serializable, Cloneable, Comparable<getPosts_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("getPosts_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 2);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new getPosts_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new getPosts_argsTupleSchemeFactory());
		}

		public String token; // required
		public String tripPath; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIP_PATH((short) 2, "tripPath");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIP_PATH
					return TRIP_PATH;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(getPosts_args.class, metaDataMap);
		}

		public getPosts_args() {
		}

		public getPosts_args(String token, String tripPath) {
			this();
			this.token = token;
			this.tripPath = tripPath;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public getPosts_args(getPosts_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
		}

		public getPosts_args deepCopy() {
			return new getPosts_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.tripPath = null;
		}

		public String getToken() {
			return this.token;
		}

		public getPosts_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public getPosts_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIP_PATH:
				return getTripPath();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIP_PATH:
				return isSetTripPath();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof getPosts_args)
				return this.equals((getPosts_args) that);
			return false;
		}

		public boolean equals(getPosts_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(getPosts_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("getPosts_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class getPosts_argsStandardSchemeFactory implements SchemeFactory {
			public getPosts_argsStandardScheme getScheme() {
				return new getPosts_argsStandardScheme();
			}
		}

		private static class getPosts_argsStandardScheme extends StandardScheme<getPosts_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, getPosts_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, getPosts_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class getPosts_argsTupleSchemeFactory implements SchemeFactory {
			public getPosts_argsTupleScheme getScheme() {
				return new getPosts_argsTupleScheme();
			}
		}

		private static class getPosts_argsTupleScheme extends TupleScheme<getPosts_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, getPosts_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTripPath()) {
					optionals.set(1);
				}
				oprot.writeBitSet(optionals, 2);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, getPosts_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(2);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
			}
		}

	}

	public static class getPosts_result implements org.apache.thrift.TBase<getPosts_result, getPosts_result._Fields>, java.io.Serializable, Cloneable, Comparable<getPosts_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("getPosts_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.LIST, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new getPosts_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new getPosts_resultTupleSchemeFactory());
		}

		public List<Post> success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Post.class))));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(getPosts_result.class, metaDataMap);
		}

		public getPosts_result() {
		}

		public getPosts_result(List<Post> success) {
			this();
			this.success = success;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public getPosts_result(getPosts_result other) {
			if (other.isSetSuccess()) {
				List<Post> __this__success = new ArrayList<Post>(other.success.size());
				for (Post other_element : other.success) {
					__this__success.add(new Post(other_element));
				}
				this.success = __this__success;
			}
		}

		public getPosts_result deepCopy() {
			return new getPosts_result(this);
		}

		@Override
		public void clear() {
			this.success = null;
		}

		public int getSuccessSize() {
			return (this.success == null) ? 0 : this.success.size();
		}

		public java.util.Iterator<Post> getSuccessIterator() {
			return (this.success == null) ? null : this.success.iterator();
		}

		public void addToSuccess(Post elem) {
			if (this.success == null) {
				this.success = new ArrayList<Post>();
			}
			this.success.add(elem);
		}

		public List<Post> getSuccess() {
			return this.success;
		}

		public getPosts_result setSuccess(List<Post> success) {
			this.success = success;
			return this;
		}

		public void unsetSuccess() {
			this.success = null;
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return this.success != null;
		}

		public void setSuccessIsSet(boolean value) {
			if (!value) {
				this.success = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((List<Post>) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return getSuccess();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof getPosts_result)
				return this.equals((getPosts_result) that);
			return false;
		}

		public boolean equals(getPosts_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true && this.isSetSuccess();
			boolean that_present_success = true && that.isSetSuccess();
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (!this.success.equals(that.success))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(getPosts_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("getPosts_result(");
			boolean first = true;

			sb.append("success:");
			if (this.success == null) {
				sb.append("null");
			} else {
				sb.append(this.success);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class getPosts_resultStandardSchemeFactory implements SchemeFactory {
			public getPosts_resultStandardScheme getScheme() {
				return new getPosts_resultStandardScheme();
			}
		}

		private static class getPosts_resultStandardScheme extends StandardScheme<getPosts_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, getPosts_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
							{
								org.apache.thrift.protocol.TList _list40 = iprot.readListBegin();
								struct.success = new ArrayList<Post>(_list40.size);
								for (int _i41 = 0; _i41 < _list40.size; ++_i41) {
									Post _elem42;
									_elem42 = new Post();
									_elem42.read(iprot);
									struct.success.add(_elem42);
								}
								iprot.readListEnd();
							}
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, getPosts_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.success != null) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					{
						oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.success.size()));
						for (Post _iter43 : struct.success) {
							_iter43.write(oprot);
						}
						oprot.writeListEnd();
					}
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class getPosts_resultTupleSchemeFactory implements SchemeFactory {
			public getPosts_resultTupleScheme getScheme() {
				return new getPosts_resultTupleScheme();
			}
		}

		private static class getPosts_resultTupleScheme extends TupleScheme<getPosts_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, getPosts_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					{
						oprot.writeI32(struct.success.size());
						for (Post _iter44 : struct.success) {
							_iter44.write(oprot);
						}
					}
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, getPosts_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					{
						org.apache.thrift.protocol.TList _list45 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
						struct.success = new ArrayList<Post>(_list45.size);
						for (int _i46 = 0; _i46 < _list45.size; ++_i46) {
							Post _elem47;
							_elem47 = new Post();
							_elem47.read(iprot);
							struct.success.add(_elem47);
						}
					}
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class getGpx_args implements org.apache.thrift.TBase<getGpx_args, getGpx_args._Fields>, java.io.Serializable, Cloneable, Comparable<getGpx_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("getGpx_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 2);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new getGpx_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new getGpx_argsTupleSchemeFactory());
		}

		public String token; // required
		public String tripPath; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIP_PATH((short) 2, "tripPath");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIP_PATH
					return TRIP_PATH;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(getGpx_args.class, metaDataMap);
		}

		public getGpx_args() {
		}

		public getGpx_args(String token, String tripPath) {
			this();
			this.token = token;
			this.tripPath = tripPath;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public getGpx_args(getGpx_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
		}

		public getGpx_args deepCopy() {
			return new getGpx_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.tripPath = null;
		}

		public String getToken() {
			return this.token;
		}

		public getGpx_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public getGpx_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIP_PATH:
				return getTripPath();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIP_PATH:
				return isSetTripPath();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof getGpx_args)
				return this.equals((getGpx_args) that);
			return false;
		}

		public boolean equals(getGpx_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(getGpx_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("getGpx_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class getGpx_argsStandardSchemeFactory implements SchemeFactory {
			public getGpx_argsStandardScheme getScheme() {
				return new getGpx_argsStandardScheme();
			}
		}

		private static class getGpx_argsStandardScheme extends StandardScheme<getGpx_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, getGpx_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, getGpx_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class getGpx_argsTupleSchemeFactory implements SchemeFactory {
			public getGpx_argsTupleScheme getScheme() {
				return new getGpx_argsTupleScheme();
			}
		}

		private static class getGpx_argsTupleScheme extends TupleScheme<getGpx_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, getGpx_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTripPath()) {
					optionals.set(1);
				}
				oprot.writeBitSet(optionals, 2);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, getGpx_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(2);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
			}
		}

	}

	public static class getGpx_result implements org.apache.thrift.TBase<getGpx_result, getGpx_result._Fields>, java.io.Serializable, Cloneable, Comparable<getGpx_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("getGpx_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.STRING, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new getGpx_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new getGpx_resultTupleSchemeFactory());
		}

		public String success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(getGpx_result.class, metaDataMap);
		}

		public getGpx_result() {
		}

		public getGpx_result(String success) {
			this();
			this.success = success;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public getGpx_result(getGpx_result other) {
			if (other.isSetSuccess()) {
				this.success = other.success;
			}
		}

		public getGpx_result deepCopy() {
			return new getGpx_result(this);
		}

		@Override
		public void clear() {
			this.success = null;
		}

		public String getSuccess() {
			return this.success;
		}

		public getGpx_result setSuccess(String success) {
			this.success = success;
			return this;
		}

		public void unsetSuccess() {
			this.success = null;
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return this.success != null;
		}

		public void setSuccessIsSet(boolean value) {
			if (!value) {
				this.success = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return getSuccess();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof getGpx_result)
				return this.equals((getGpx_result) that);
			return false;
		}

		public boolean equals(getGpx_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true && this.isSetSuccess();
			boolean that_present_success = true && that.isSetSuccess();
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (!this.success.equals(that.success))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(getGpx_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("getGpx_result(");
			boolean first = true;

			sb.append("success:");
			if (this.success == null) {
				sb.append("null");
			} else {
				sb.append(this.success);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class getGpx_resultStandardSchemeFactory implements SchemeFactory {
			public getGpx_resultStandardScheme getScheme() {
				return new getGpx_resultStandardScheme();
			}
		}

		private static class getGpx_resultStandardScheme extends StandardScheme<getGpx_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, getGpx_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.success = iprot.readString();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, getGpx_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.success != null) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeString(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class getGpx_resultTupleSchemeFactory implements SchemeFactory {
			public getGpx_resultTupleScheme getScheme() {
				return new getGpx_resultTupleScheme();
			}
		}

		private static class getGpx_resultTupleScheme extends TupleScheme<getGpx_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, getGpx_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeString(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, getGpx_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readString();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class zipTrip_args implements org.apache.thrift.TBase<zipTrip_args, zipTrip_args._Fields>, java.io.Serializable, Cloneable, Comparable<zipTrip_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("zipTrip_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 2);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new zipTrip_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new zipTrip_argsTupleSchemeFactory());
		}

		public String token; // required
		public String tripPath; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIP_PATH((short) 2, "tripPath");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIP_PATH
					return TRIP_PATH;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(zipTrip_args.class, metaDataMap);
		}

		public zipTrip_args() {
		}

		public zipTrip_args(String token, String tripPath) {
			this();
			this.token = token;
			this.tripPath = tripPath;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public zipTrip_args(zipTrip_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
		}

		public zipTrip_args deepCopy() {
			return new zipTrip_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.tripPath = null;
		}

		public String getToken() {
			return this.token;
		}

		public zipTrip_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public zipTrip_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIP_PATH:
				return getTripPath();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIP_PATH:
				return isSetTripPath();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof zipTrip_args)
				return this.equals((zipTrip_args) that);
			return false;
		}

		public boolean equals(zipTrip_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(zipTrip_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("zipTrip_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class zipTrip_argsStandardSchemeFactory implements SchemeFactory {
			public zipTrip_argsStandardScheme getScheme() {
				return new zipTrip_argsStandardScheme();
			}
		}

		private static class zipTrip_argsStandardScheme extends StandardScheme<zipTrip_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, zipTrip_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, zipTrip_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class zipTrip_argsTupleSchemeFactory implements SchemeFactory {
			public zipTrip_argsTupleScheme getScheme() {
				return new zipTrip_argsTupleScheme();
			}
		}

		private static class zipTrip_argsTupleScheme extends TupleScheme<zipTrip_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, zipTrip_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTripPath()) {
					optionals.set(1);
				}
				oprot.writeBitSet(optionals, 2);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, zipTrip_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(2);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
			}
		}

	}

	public static class zipTrip_result implements org.apache.thrift.TBase<zipTrip_result, zipTrip_result._Fields>, java.io.Serializable, Cloneable, Comparable<zipTrip_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("zipTrip_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.STRING, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new zipTrip_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new zipTrip_resultTupleSchemeFactory());
		}

		public String success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(zipTrip_result.class, metaDataMap);
		}

		public zipTrip_result() {
		}

		public zipTrip_result(String success) {
			this();
			this.success = success;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public zipTrip_result(zipTrip_result other) {
			if (other.isSetSuccess()) {
				this.success = other.success;
			}
		}

		public zipTrip_result deepCopy() {
			return new zipTrip_result(this);
		}

		@Override
		public void clear() {
			this.success = null;
		}

		public String getSuccess() {
			return this.success;
		}

		public zipTrip_result setSuccess(String success) {
			this.success = success;
			return this;
		}

		public void unsetSuccess() {
			this.success = null;
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return this.success != null;
		}

		public void setSuccessIsSet(boolean value) {
			if (!value) {
				this.success = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return getSuccess();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof zipTrip_result)
				return this.equals((zipTrip_result) that);
			return false;
		}

		public boolean equals(zipTrip_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true && this.isSetSuccess();
			boolean that_present_success = true && that.isSetSuccess();
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (!this.success.equals(that.success))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(zipTrip_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("zipTrip_result(");
			boolean first = true;

			sb.append("success:");
			if (this.success == null) {
				sb.append("null");
			} else {
				sb.append(this.success);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class zipTrip_resultStandardSchemeFactory implements SchemeFactory {
			public zipTrip_resultStandardScheme getScheme() {
				return new zipTrip_resultStandardScheme();
			}
		}

		private static class zipTrip_resultStandardScheme extends StandardScheme<zipTrip_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, zipTrip_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.success = iprot.readString();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, zipTrip_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.success != null) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeString(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class zipTrip_resultTupleSchemeFactory implements SchemeFactory {
			public zipTrip_resultTupleScheme getScheme() {
				return new zipTrip_resultTupleScheme();
			}
		}

		private static class zipTrip_resultTupleScheme extends TupleScheme<zipTrip_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, zipTrip_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeString(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, zipTrip_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readString();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class edit_trip_note_args implements org.apache.thrift.TBase<edit_trip_note_args, edit_trip_note_args._Fields>, java.io.Serializable, Cloneable, Comparable<edit_trip_note_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("edit_trip_note_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 2);
		private static final org.apache.thrift.protocol.TField NOTE_FIELD_DESC = new org.apache.thrift.protocol.TField("note", org.apache.thrift.protocol.TType.STRING, (short) 3);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new edit_trip_note_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new edit_trip_note_argsTupleSchemeFactory());
		}

		public String token; // required
		public String tripPath; // required
		public String note; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIP_PATH((short) 2, "tripPath"), NOTE((short) 3, "note");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIP_PATH
					return TRIP_PATH;
				case 3: // NOTE
					return NOTE;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.NOTE, new org.apache.thrift.meta_data.FieldMetaData("note", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(edit_trip_note_args.class, metaDataMap);
		}

		public edit_trip_note_args() {
		}

		public edit_trip_note_args(String token, String tripPath, String note) {
			this();
			this.token = token;
			this.tripPath = tripPath;
			this.note = note;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public edit_trip_note_args(edit_trip_note_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
			if (other.isSetNote()) {
				this.note = other.note;
			}
		}

		public edit_trip_note_args deepCopy() {
			return new edit_trip_note_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.tripPath = null;
			this.note = null;
		}

		public String getToken() {
			return this.token;
		}

		public edit_trip_note_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public edit_trip_note_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public String getNote() {
			return this.note;
		}

		public edit_trip_note_args setNote(String note) {
			this.note = note;
			return this;
		}

		public void unsetNote() {
			this.note = null;
		}

		/**
		 * Returns true if field note is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetNote() {
			return this.note != null;
		}

		public void setNoteIsSet(boolean value) {
			if (!value) {
				this.note = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			case NOTE:
				if (value == null) {
					unsetNote();
				} else {
					setNote((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIP_PATH:
				return getTripPath();

			case NOTE:
				return getNote();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIP_PATH:
				return isSetTripPath();
			case NOTE:
				return isSetNote();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof edit_trip_note_args)
				return this.equals((edit_trip_note_args) that);
			return false;
		}

		public boolean equals(edit_trip_note_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			boolean this_present_note = true && this.isSetNote();
			boolean that_present_note = true && that.isSetNote();
			if (this_present_note || that_present_note) {
				if (!(this_present_note && that_present_note))
					return false;
				if (!this.note.equals(that.note))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(edit_trip_note_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetNote()).compareTo(other.isSetNote());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetNote()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.note, other.note);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("edit_trip_note_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("note:");
			if (this.note == null) {
				sb.append("null");
			} else {
				sb.append(this.note);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class edit_trip_note_argsStandardSchemeFactory implements SchemeFactory {
			public edit_trip_note_argsStandardScheme getScheme() {
				return new edit_trip_note_argsStandardScheme();
			}
		}

		private static class edit_trip_note_argsStandardScheme extends StandardScheme<edit_trip_note_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, edit_trip_note_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 3: // NOTE
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.note = iprot.readString();
							struct.setNoteIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, edit_trip_note_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				if (struct.note != null) {
					oprot.writeFieldBegin(NOTE_FIELD_DESC);
					oprot.writeString(struct.note);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class edit_trip_note_argsTupleSchemeFactory implements SchemeFactory {
			public edit_trip_note_argsTupleScheme getScheme() {
				return new edit_trip_note_argsTupleScheme();
			}
		}

		private static class edit_trip_note_argsTupleScheme extends TupleScheme<edit_trip_note_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, edit_trip_note_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTripPath()) {
					optionals.set(1);
				}
				if (struct.isSetNote()) {
					optionals.set(2);
				}
				oprot.writeBitSet(optionals, 3);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
				if (struct.isSetNote()) {
					oprot.writeString(struct.note);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, edit_trip_note_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(3);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
				if (incoming.get(2)) {
					struct.note = iprot.readString();
					struct.setNoteIsSet(true);
				}
			}
		}

	}

	public static class edit_trip_note_result implements org.apache.thrift.TBase<edit_trip_note_result, edit_trip_note_result._Fields>, java.io.Serializable, Cloneable, Comparable<edit_trip_note_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("edit_trip_note_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.BOOL, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new edit_trip_note_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new edit_trip_note_resultTupleSchemeFactory());
		}

		public boolean success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __SUCCESS_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(edit_trip_note_result.class, metaDataMap);
		}

		public edit_trip_note_result() {
		}

		public edit_trip_note_result(boolean success) {
			this();
			this.success = success;
			setSuccessIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public edit_trip_note_result(edit_trip_note_result other) {
			__isset_bitfield = other.__isset_bitfield;
			this.success = other.success;
		}

		public edit_trip_note_result deepCopy() {
			return new edit_trip_note_result(this);
		}

		@Override
		public void clear() {
			setSuccessIsSet(false);
			this.success = false;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public edit_trip_note_result setSuccess(boolean success) {
			this.success = success;
			setSuccessIsSet(true);
			return this;
		}

		public void unsetSuccess() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return EncodingUtils.testBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		public void setSuccessIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SUCCESS_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Boolean) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return Boolean.valueOf(isSuccess());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof edit_trip_note_result)
				return this.equals((edit_trip_note_result) that);
			return false;
		}

		public boolean equals(edit_trip_note_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true;
			boolean that_present_success = true;
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (this.success != that.success)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(edit_trip_note_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("edit_trip_note_result(");
			boolean first = true;

			sb.append("success:");
			sb.append(this.success);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class edit_trip_note_resultStandardSchemeFactory implements SchemeFactory {
			public edit_trip_note_resultStandardScheme getScheme() {
				return new edit_trip_note_resultStandardScheme();
			}
		}

		private static class edit_trip_note_resultStandardScheme extends StandardScheme<edit_trip_note_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, edit_trip_note_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.success = iprot.readBool();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, edit_trip_note_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.isSetSuccess()) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeBool(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class edit_trip_note_resultTupleSchemeFactory implements SchemeFactory {
			public edit_trip_note_resultTupleScheme getScheme() {
				return new edit_trip_note_resultTupleScheme();
			}
		}

		private static class edit_trip_note_resultTupleScheme extends TupleScheme<edit_trip_note_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, edit_trip_note_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeBool(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, edit_trip_note_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readBool();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class edi_poi_diary_args implements org.apache.thrift.TBase<edi_poi_diary_args, edi_poi_diary_args._Fields>, java.io.Serializable, Cloneable, Comparable<edi_poi_diary_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("edi_poi_diary_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 2);
		private static final org.apache.thrift.protocol.TField POI_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("poiName", org.apache.thrift.protocol.TType.STRING, (short) 3);
		private static final org.apache.thrift.protocol.TField DIARY_FIELD_DESC = new org.apache.thrift.protocol.TField("diary", org.apache.thrift.protocol.TType.STRING, (short) 4);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new edi_poi_diary_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new edi_poi_diary_argsTupleSchemeFactory());
		}

		public String token; // required
		public String tripPath; // required
		public String poiName; // required
		public String diary; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIP_PATH((short) 2, "tripPath"), POI_NAME((short) 3, "poiName"), DIARY((short) 4, "diary");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIP_PATH
					return TRIP_PATH;
				case 3: // POI_NAME
					return POI_NAME;
				case 4: // DIARY
					return DIARY;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.POI_NAME, new org.apache.thrift.meta_data.FieldMetaData("poiName", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.DIARY, new org.apache.thrift.meta_data.FieldMetaData("diary", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(edi_poi_diary_args.class, metaDataMap);
		}

		public edi_poi_diary_args() {
		}

		public edi_poi_diary_args(String token, String tripPath, String poiName, String diary) {
			this();
			this.token = token;
			this.tripPath = tripPath;
			this.poiName = poiName;
			this.diary = diary;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public edi_poi_diary_args(edi_poi_diary_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
			if (other.isSetPoiName()) {
				this.poiName = other.poiName;
			}
			if (other.isSetDiary()) {
				this.diary = other.diary;
			}
		}

		public edi_poi_diary_args deepCopy() {
			return new edi_poi_diary_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.tripPath = null;
			this.poiName = null;
			this.diary = null;
		}

		public String getToken() {
			return this.token;
		}

		public edi_poi_diary_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public edi_poi_diary_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public String getPoiName() {
			return this.poiName;
		}

		public edi_poi_diary_args setPoiName(String poiName) {
			this.poiName = poiName;
			return this;
		}

		public void unsetPoiName() {
			this.poiName = null;
		}

		/**
		 * Returns true if field poiName is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetPoiName() {
			return this.poiName != null;
		}

		public void setPoiNameIsSet(boolean value) {
			if (!value) {
				this.poiName = null;
			}
		}

		public String getDiary() {
			return this.diary;
		}

		public edi_poi_diary_args setDiary(String diary) {
			this.diary = diary;
			return this;
		}

		public void unsetDiary() {
			this.diary = null;
		}

		/**
		 * Returns true if field diary is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetDiary() {
			return this.diary != null;
		}

		public void setDiaryIsSet(boolean value) {
			if (!value) {
				this.diary = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			case POI_NAME:
				if (value == null) {
					unsetPoiName();
				} else {
					setPoiName((String) value);
				}
				break;

			case DIARY:
				if (value == null) {
					unsetDiary();
				} else {
					setDiary((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIP_PATH:
				return getTripPath();

			case POI_NAME:
				return getPoiName();

			case DIARY:
				return getDiary();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIP_PATH:
				return isSetTripPath();
			case POI_NAME:
				return isSetPoiName();
			case DIARY:
				return isSetDiary();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof edi_poi_diary_args)
				return this.equals((edi_poi_diary_args) that);
			return false;
		}

		public boolean equals(edi_poi_diary_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			boolean this_present_poiName = true && this.isSetPoiName();
			boolean that_present_poiName = true && that.isSetPoiName();
			if (this_present_poiName || that_present_poiName) {
				if (!(this_present_poiName && that_present_poiName))
					return false;
				if (!this.poiName.equals(that.poiName))
					return false;
			}

			boolean this_present_diary = true && this.isSetDiary();
			boolean that_present_diary = true && that.isSetDiary();
			if (this_present_diary || that_present_diary) {
				if (!(this_present_diary && that_present_diary))
					return false;
				if (!this.diary.equals(that.diary))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(edi_poi_diary_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetPoiName()).compareTo(other.isSetPoiName());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetPoiName()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.poiName, other.poiName);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetDiary()).compareTo(other.isSetDiary());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetDiary()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.diary, other.diary);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("edi_poi_diary_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("poiName:");
			if (this.poiName == null) {
				sb.append("null");
			} else {
				sb.append(this.poiName);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("diary:");
			if (this.diary == null) {
				sb.append("null");
			} else {
				sb.append(this.diary);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class edi_poi_diary_argsStandardSchemeFactory implements SchemeFactory {
			public edi_poi_diary_argsStandardScheme getScheme() {
				return new edi_poi_diary_argsStandardScheme();
			}
		}

		private static class edi_poi_diary_argsStandardScheme extends StandardScheme<edi_poi_diary_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, edi_poi_diary_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 3: // POI_NAME
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.poiName = iprot.readString();
							struct.setPoiNameIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 4: // DIARY
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.diary = iprot.readString();
							struct.setDiaryIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, edi_poi_diary_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				if (struct.poiName != null) {
					oprot.writeFieldBegin(POI_NAME_FIELD_DESC);
					oprot.writeString(struct.poiName);
					oprot.writeFieldEnd();
				}
				if (struct.diary != null) {
					oprot.writeFieldBegin(DIARY_FIELD_DESC);
					oprot.writeString(struct.diary);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class edi_poi_diary_argsTupleSchemeFactory implements SchemeFactory {
			public edi_poi_diary_argsTupleScheme getScheme() {
				return new edi_poi_diary_argsTupleScheme();
			}
		}

		private static class edi_poi_diary_argsTupleScheme extends TupleScheme<edi_poi_diary_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, edi_poi_diary_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTripPath()) {
					optionals.set(1);
				}
				if (struct.isSetPoiName()) {
					optionals.set(2);
				}
				if (struct.isSetDiary()) {
					optionals.set(3);
				}
				oprot.writeBitSet(optionals, 4);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
				if (struct.isSetPoiName()) {
					oprot.writeString(struct.poiName);
				}
				if (struct.isSetDiary()) {
					oprot.writeString(struct.diary);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, edi_poi_diary_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(4);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
				if (incoming.get(2)) {
					struct.poiName = iprot.readString();
					struct.setPoiNameIsSet(true);
				}
				if (incoming.get(3)) {
					struct.diary = iprot.readString();
					struct.setDiaryIsSet(true);
				}
			}
		}

	}

	public static class edi_poi_diary_result implements org.apache.thrift.TBase<edi_poi_diary_result, edi_poi_diary_result._Fields>, java.io.Serializable, Cloneable, Comparable<edi_poi_diary_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("edi_poi_diary_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.BOOL, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new edi_poi_diary_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new edi_poi_diary_resultTupleSchemeFactory());
		}

		public boolean success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __SUCCESS_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(edi_poi_diary_result.class, metaDataMap);
		}

		public edi_poi_diary_result() {
		}

		public edi_poi_diary_result(boolean success) {
			this();
			this.success = success;
			setSuccessIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public edi_poi_diary_result(edi_poi_diary_result other) {
			__isset_bitfield = other.__isset_bitfield;
			this.success = other.success;
		}

		public edi_poi_diary_result deepCopy() {
			return new edi_poi_diary_result(this);
		}

		@Override
		public void clear() {
			setSuccessIsSet(false);
			this.success = false;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public edi_poi_diary_result setSuccess(boolean success) {
			this.success = success;
			setSuccessIsSet(true);
			return this;
		}

		public void unsetSuccess() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return EncodingUtils.testBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		public void setSuccessIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SUCCESS_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Boolean) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return Boolean.valueOf(isSuccess());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof edi_poi_diary_result)
				return this.equals((edi_poi_diary_result) that);
			return false;
		}

		public boolean equals(edi_poi_diary_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true;
			boolean that_present_success = true;
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (this.success != that.success)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(edi_poi_diary_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("edi_poi_diary_result(");
			boolean first = true;

			sb.append("success:");
			sb.append(this.success);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class edi_poi_diary_resultStandardSchemeFactory implements SchemeFactory {
			public edi_poi_diary_resultStandardScheme getScheme() {
				return new edi_poi_diary_resultStandardScheme();
			}
		}

		private static class edi_poi_diary_resultStandardScheme extends StandardScheme<edi_poi_diary_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, edi_poi_diary_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.success = iprot.readBool();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, edi_poi_diary_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.isSetSuccess()) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeBool(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class edi_poi_diary_resultTupleSchemeFactory implements SchemeFactory {
			public edi_poi_diary_resultTupleScheme getScheme() {
				return new edi_poi_diary_resultTupleScheme();
			}
		}

		private static class edi_poi_diary_resultTupleScheme extends TupleScheme<edi_poi_diary_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, edi_poi_diary_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeBool(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, edi_poi_diary_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readBool();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class edi_poi_basicinformation_args implements org.apache.thrift.TBase<edi_poi_basicinformation_args, edi_poi_basicinformation_args._Fields>, java.io.Serializable, Cloneable, Comparable<edi_poi_basicinformation_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("edi_poi_basicinformation_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 2);
		private static final org.apache.thrift.protocol.TField POI_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("poiName", org.apache.thrift.protocol.TType.STRING, (short) 3);
		private static final org.apache.thrift.protocol.TField CONTENT_FIELD_DESC = new org.apache.thrift.protocol.TField("content", org.apache.thrift.protocol.TType.STRING, (short) 4);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new edi_poi_basicinformation_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new edi_poi_basicinformation_argsTupleSchemeFactory());
		}

		public String token; // required
		public String tripPath; // required
		public String poiName; // required
		public String content; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIP_PATH((short) 2, "tripPath"), POI_NAME((short) 3, "poiName"), CONTENT((short) 4, "content");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIP_PATH
					return TRIP_PATH;
				case 3: // POI_NAME
					return POI_NAME;
				case 4: // CONTENT
					return CONTENT;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.POI_NAME, new org.apache.thrift.meta_data.FieldMetaData("poiName", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.CONTENT, new org.apache.thrift.meta_data.FieldMetaData("content", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(edi_poi_basicinformation_args.class, metaDataMap);
		}

		public edi_poi_basicinformation_args() {
		}

		public edi_poi_basicinformation_args(String token, String tripPath, String poiName, String content) {
			this();
			this.token = token;
			this.tripPath = tripPath;
			this.poiName = poiName;
			this.content = content;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public edi_poi_basicinformation_args(edi_poi_basicinformation_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
			if (other.isSetPoiName()) {
				this.poiName = other.poiName;
			}
			if (other.isSetContent()) {
				this.content = other.content;
			}
		}

		public edi_poi_basicinformation_args deepCopy() {
			return new edi_poi_basicinformation_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.tripPath = null;
			this.poiName = null;
			this.content = null;
		}

		public String getToken() {
			return this.token;
		}

		public edi_poi_basicinformation_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public edi_poi_basicinformation_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public String getPoiName() {
			return this.poiName;
		}

		public edi_poi_basicinformation_args setPoiName(String poiName) {
			this.poiName = poiName;
			return this;
		}

		public void unsetPoiName() {
			this.poiName = null;
		}

		/**
		 * Returns true if field poiName is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetPoiName() {
			return this.poiName != null;
		}

		public void setPoiNameIsSet(boolean value) {
			if (!value) {
				this.poiName = null;
			}
		}

		public String getContent() {
			return this.content;
		}

		public edi_poi_basicinformation_args setContent(String content) {
			this.content = content;
			return this;
		}

		public void unsetContent() {
			this.content = null;
		}

		/**
		 * Returns true if field content is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetContent() {
			return this.content != null;
		}

		public void setContentIsSet(boolean value) {
			if (!value) {
				this.content = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			case POI_NAME:
				if (value == null) {
					unsetPoiName();
				} else {
					setPoiName((String) value);
				}
				break;

			case CONTENT:
				if (value == null) {
					unsetContent();
				} else {
					setContent((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIP_PATH:
				return getTripPath();

			case POI_NAME:
				return getPoiName();

			case CONTENT:
				return getContent();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIP_PATH:
				return isSetTripPath();
			case POI_NAME:
				return isSetPoiName();
			case CONTENT:
				return isSetContent();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof edi_poi_basicinformation_args)
				return this.equals((edi_poi_basicinformation_args) that);
			return false;
		}

		public boolean equals(edi_poi_basicinformation_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			boolean this_present_poiName = true && this.isSetPoiName();
			boolean that_present_poiName = true && that.isSetPoiName();
			if (this_present_poiName || that_present_poiName) {
				if (!(this_present_poiName && that_present_poiName))
					return false;
				if (!this.poiName.equals(that.poiName))
					return false;
			}

			boolean this_present_content = true && this.isSetContent();
			boolean that_present_content = true && that.isSetContent();
			if (this_present_content || that_present_content) {
				if (!(this_present_content && that_present_content))
					return false;
				if (!this.content.equals(that.content))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(edi_poi_basicinformation_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetPoiName()).compareTo(other.isSetPoiName());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetPoiName()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.poiName, other.poiName);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetContent()).compareTo(other.isSetContent());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetContent()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.content, other.content);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("edi_poi_basicinformation_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("poiName:");
			if (this.poiName == null) {
				sb.append("null");
			} else {
				sb.append(this.poiName);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("content:");
			if (this.content == null) {
				sb.append("null");
			} else {
				sb.append(this.content);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class edi_poi_basicinformation_argsStandardSchemeFactory implements SchemeFactory {
			public edi_poi_basicinformation_argsStandardScheme getScheme() {
				return new edi_poi_basicinformation_argsStandardScheme();
			}
		}

		private static class edi_poi_basicinformation_argsStandardScheme extends StandardScheme<edi_poi_basicinformation_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, edi_poi_basicinformation_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 3: // POI_NAME
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.poiName = iprot.readString();
							struct.setPoiNameIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 4: // CONTENT
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.content = iprot.readString();
							struct.setContentIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, edi_poi_basicinformation_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				if (struct.poiName != null) {
					oprot.writeFieldBegin(POI_NAME_FIELD_DESC);
					oprot.writeString(struct.poiName);
					oprot.writeFieldEnd();
				}
				if (struct.content != null) {
					oprot.writeFieldBegin(CONTENT_FIELD_DESC);
					oprot.writeString(struct.content);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class edi_poi_basicinformation_argsTupleSchemeFactory implements SchemeFactory {
			public edi_poi_basicinformation_argsTupleScheme getScheme() {
				return new edi_poi_basicinformation_argsTupleScheme();
			}
		}

		private static class edi_poi_basicinformation_argsTupleScheme extends TupleScheme<edi_poi_basicinformation_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, edi_poi_basicinformation_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTripPath()) {
					optionals.set(1);
				}
				if (struct.isSetPoiName()) {
					optionals.set(2);
				}
				if (struct.isSetContent()) {
					optionals.set(3);
				}
				oprot.writeBitSet(optionals, 4);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
				if (struct.isSetPoiName()) {
					oprot.writeString(struct.poiName);
				}
				if (struct.isSetContent()) {
					oprot.writeString(struct.content);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, edi_poi_basicinformation_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(4);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
				if (incoming.get(2)) {
					struct.poiName = iprot.readString();
					struct.setPoiNameIsSet(true);
				}
				if (incoming.get(3)) {
					struct.content = iprot.readString();
					struct.setContentIsSet(true);
				}
			}
		}

	}

	public static class edi_poi_basicinformation_result implements org.apache.thrift.TBase<edi_poi_basicinformation_result, edi_poi_basicinformation_result._Fields>, java.io.Serializable, Cloneable, Comparable<edi_poi_basicinformation_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("edi_poi_basicinformation_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.BOOL, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new edi_poi_basicinformation_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new edi_poi_basicinformation_resultTupleSchemeFactory());
		}

		public boolean success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __SUCCESS_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(edi_poi_basicinformation_result.class, metaDataMap);
		}

		public edi_poi_basicinformation_result() {
		}

		public edi_poi_basicinformation_result(boolean success) {
			this();
			this.success = success;
			setSuccessIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public edi_poi_basicinformation_result(edi_poi_basicinformation_result other) {
			__isset_bitfield = other.__isset_bitfield;
			this.success = other.success;
		}

		public edi_poi_basicinformation_result deepCopy() {
			return new edi_poi_basicinformation_result(this);
		}

		@Override
		public void clear() {
			setSuccessIsSet(false);
			this.success = false;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public edi_poi_basicinformation_result setSuccess(boolean success) {
			this.success = success;
			setSuccessIsSet(true);
			return this;
		}

		public void unsetSuccess() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return EncodingUtils.testBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		public void setSuccessIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SUCCESS_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Boolean) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return Boolean.valueOf(isSuccess());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof edi_poi_basicinformation_result)
				return this.equals((edi_poi_basicinformation_result) that);
			return false;
		}

		public boolean equals(edi_poi_basicinformation_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true;
			boolean that_present_success = true;
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (this.success != that.success)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(edi_poi_basicinformation_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("edi_poi_basicinformation_result(");
			boolean first = true;

			sb.append("success:");
			sb.append(this.success);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class edi_poi_basicinformation_resultStandardSchemeFactory implements SchemeFactory {
			public edi_poi_basicinformation_resultStandardScheme getScheme() {
				return new edi_poi_basicinformation_resultStandardScheme();
			}
		}

		private static class edi_poi_basicinformation_resultStandardScheme extends StandardScheme<edi_poi_basicinformation_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, edi_poi_basicinformation_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.success = iprot.readBool();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, edi_poi_basicinformation_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.isSetSuccess()) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeBool(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class edi_poi_basicinformation_resultTupleSchemeFactory implements SchemeFactory {
			public edi_poi_basicinformation_resultTupleScheme getScheme() {
				return new edi_poi_basicinformation_resultTupleScheme();
			}
		}

		private static class edi_poi_basicinformation_resultTupleScheme extends TupleScheme<edi_poi_basicinformation_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, edi_poi_basicinformation_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeBool(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, edi_poi_basicinformation_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readBool();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class delete_trip_args implements org.apache.thrift.TBase<delete_trip_args, delete_trip_args._Fields>, java.io.Serializable, Cloneable, Comparable<delete_trip_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("delete_trip_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 2);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new delete_trip_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new delete_trip_argsTupleSchemeFactory());
		}

		public String token; // required
		public String tripPath; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIP_PATH((short) 2, "tripPath");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIP_PATH
					return TRIP_PATH;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(delete_trip_args.class, metaDataMap);
		}

		public delete_trip_args() {
		}

		public delete_trip_args(String token, String tripPath) {
			this();
			this.token = token;
			this.tripPath = tripPath;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public delete_trip_args(delete_trip_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
		}

		public delete_trip_args deepCopy() {
			return new delete_trip_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.tripPath = null;
		}

		public String getToken() {
			return this.token;
		}

		public delete_trip_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public delete_trip_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIP_PATH:
				return getTripPath();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIP_PATH:
				return isSetTripPath();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof delete_trip_args)
				return this.equals((delete_trip_args) that);
			return false;
		}

		public boolean equals(delete_trip_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(delete_trip_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("delete_trip_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class delete_trip_argsStandardSchemeFactory implements SchemeFactory {
			public delete_trip_argsStandardScheme getScheme() {
				return new delete_trip_argsStandardScheme();
			}
		}

		private static class delete_trip_argsStandardScheme extends StandardScheme<delete_trip_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, delete_trip_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, delete_trip_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class delete_trip_argsTupleSchemeFactory implements SchemeFactory {
			public delete_trip_argsTupleScheme getScheme() {
				return new delete_trip_argsTupleScheme();
			}
		}

		private static class delete_trip_argsTupleScheme extends TupleScheme<delete_trip_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, delete_trip_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTripPath()) {
					optionals.set(1);
				}
				oprot.writeBitSet(optionals, 2);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, delete_trip_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(2);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
			}
		}

	}

	public static class delete_trip_result implements org.apache.thrift.TBase<delete_trip_result, delete_trip_result._Fields>, java.io.Serializable, Cloneable, Comparable<delete_trip_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("delete_trip_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.BOOL, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new delete_trip_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new delete_trip_resultTupleSchemeFactory());
		}

		public boolean success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __SUCCESS_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(delete_trip_result.class, metaDataMap);
		}

		public delete_trip_result() {
		}

		public delete_trip_result(boolean success) {
			this();
			this.success = success;
			setSuccessIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public delete_trip_result(delete_trip_result other) {
			__isset_bitfield = other.__isset_bitfield;
			this.success = other.success;
		}

		public delete_trip_result deepCopy() {
			return new delete_trip_result(this);
		}

		@Override
		public void clear() {
			setSuccessIsSet(false);
			this.success = false;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public delete_trip_result setSuccess(boolean success) {
			this.success = success;
			setSuccessIsSet(true);
			return this;
		}

		public void unsetSuccess() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return EncodingUtils.testBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		public void setSuccessIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SUCCESS_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Boolean) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return Boolean.valueOf(isSuccess());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof delete_trip_result)
				return this.equals((delete_trip_result) that);
			return false;
		}

		public boolean equals(delete_trip_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true;
			boolean that_present_success = true;
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (this.success != that.success)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(delete_trip_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("delete_trip_result(");
			boolean first = true;

			sb.append("success:");
			sb.append(this.success);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class delete_trip_resultStandardSchemeFactory implements SchemeFactory {
			public delete_trip_resultStandardScheme getScheme() {
				return new delete_trip_resultStandardScheme();
			}
		}

		private static class delete_trip_resultStandardScheme extends StandardScheme<delete_trip_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, delete_trip_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.success = iprot.readBool();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, delete_trip_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.isSetSuccess()) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeBool(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class delete_trip_resultTupleSchemeFactory implements SchemeFactory {
			public delete_trip_resultTupleScheme getScheme() {
				return new delete_trip_resultTupleScheme();
			}
		}

		private static class delete_trip_resultTupleScheme extends TupleScheme<delete_trip_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, delete_trip_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeBool(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, delete_trip_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readBool();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class rename_poi_file_args implements org.apache.thrift.TBase<rename_poi_file_args, rename_poi_file_args._Fields>, java.io.Serializable, Cloneable, Comparable<rename_poi_file_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("rename_poi_file_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("path", org.apache.thrift.protocol.TType.STRING, (short) 2);
		private static final org.apache.thrift.protocol.TField NEW_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("newName", org.apache.thrift.protocol.TType.STRING, (short) 3);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new rename_poi_file_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new rename_poi_file_argsTupleSchemeFactory());
		}

		public String token; // required
		public String path; // required
		public String newName; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), PATH((short) 2, "path"), NEW_NAME((short) 3, "newName");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // PATH
					return PATH;
				case 3: // NEW_NAME
					return NEW_NAME;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.PATH, new org.apache.thrift.meta_data.FieldMetaData("path", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.NEW_NAME, new org.apache.thrift.meta_data.FieldMetaData("newName", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(rename_poi_file_args.class, metaDataMap);
		}

		public rename_poi_file_args() {
		}

		public rename_poi_file_args(String token, String path, String newName) {
			this();
			this.token = token;
			this.path = path;
			this.newName = newName;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public rename_poi_file_args(rename_poi_file_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetPath()) {
				this.path = other.path;
			}
			if (other.isSetNewName()) {
				this.newName = other.newName;
			}
		}

		public rename_poi_file_args deepCopy() {
			return new rename_poi_file_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.path = null;
			this.newName = null;
		}

		public String getToken() {
			return this.token;
		}

		public rename_poi_file_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getPath() {
			return this.path;
		}

		public rename_poi_file_args setPath(String path) {
			this.path = path;
			return this;
		}

		public void unsetPath() {
			this.path = null;
		}

		/**
		 * Returns true if field path is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetPath() {
			return this.path != null;
		}

		public void setPathIsSet(boolean value) {
			if (!value) {
				this.path = null;
			}
		}

		public String getNewName() {
			return this.newName;
		}

		public rename_poi_file_args setNewName(String newName) {
			this.newName = newName;
			return this;
		}

		public void unsetNewName() {
			this.newName = null;
		}

		/**
		 * Returns true if field newName is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetNewName() {
			return this.newName != null;
		}

		public void setNewNameIsSet(boolean value) {
			if (!value) {
				this.newName = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case PATH:
				if (value == null) {
					unsetPath();
				} else {
					setPath((String) value);
				}
				break;

			case NEW_NAME:
				if (value == null) {
					unsetNewName();
				} else {
					setNewName((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case PATH:
				return getPath();

			case NEW_NAME:
				return getNewName();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case PATH:
				return isSetPath();
			case NEW_NAME:
				return isSetNewName();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof rename_poi_file_args)
				return this.equals((rename_poi_file_args) that);
			return false;
		}

		public boolean equals(rename_poi_file_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_path = true && this.isSetPath();
			boolean that_present_path = true && that.isSetPath();
			if (this_present_path || that_present_path) {
				if (!(this_present_path && that_present_path))
					return false;
				if (!this.path.equals(that.path))
					return false;
			}

			boolean this_present_newName = true && this.isSetNewName();
			boolean that_present_newName = true && that.isSetNewName();
			if (this_present_newName || that_present_newName) {
				if (!(this_present_newName && that_present_newName))
					return false;
				if (!this.newName.equals(that.newName))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(rename_poi_file_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetPath()).compareTo(other.isSetPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.path, other.path);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetNewName()).compareTo(other.isSetNewName());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetNewName()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.newName, other.newName);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("rename_poi_file_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("path:");
			if (this.path == null) {
				sb.append("null");
			} else {
				sb.append(this.path);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("newName:");
			if (this.newName == null) {
				sb.append("null");
			} else {
				sb.append(this.newName);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class rename_poi_file_argsStandardSchemeFactory implements SchemeFactory {
			public rename_poi_file_argsStandardScheme getScheme() {
				return new rename_poi_file_argsStandardScheme();
			}
		}

		private static class rename_poi_file_argsStandardScheme extends StandardScheme<rename_poi_file_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, rename_poi_file_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.path = iprot.readString();
							struct.setPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 3: // NEW_NAME
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.newName = iprot.readString();
							struct.setNewNameIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, rename_poi_file_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.path != null) {
					oprot.writeFieldBegin(PATH_FIELD_DESC);
					oprot.writeString(struct.path);
					oprot.writeFieldEnd();
				}
				if (struct.newName != null) {
					oprot.writeFieldBegin(NEW_NAME_FIELD_DESC);
					oprot.writeString(struct.newName);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class rename_poi_file_argsTupleSchemeFactory implements SchemeFactory {
			public rename_poi_file_argsTupleScheme getScheme() {
				return new rename_poi_file_argsTupleScheme();
			}
		}

		private static class rename_poi_file_argsTupleScheme extends TupleScheme<rename_poi_file_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, rename_poi_file_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetPath()) {
					optionals.set(1);
				}
				if (struct.isSetNewName()) {
					optionals.set(2);
				}
				oprot.writeBitSet(optionals, 3);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetPath()) {
					oprot.writeString(struct.path);
				}
				if (struct.isSetNewName()) {
					oprot.writeString(struct.newName);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, rename_poi_file_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(3);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.path = iprot.readString();
					struct.setPathIsSet(true);
				}
				if (incoming.get(2)) {
					struct.newName = iprot.readString();
					struct.setNewNameIsSet(true);
				}
			}
		}

	}

	public static class rename_poi_file_result implements org.apache.thrift.TBase<rename_poi_file_result, rename_poi_file_result._Fields>, java.io.Serializable, Cloneable, Comparable<rename_poi_file_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("rename_poi_file_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.BOOL, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new rename_poi_file_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new rename_poi_file_resultTupleSchemeFactory());
		}

		public boolean success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __SUCCESS_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(rename_poi_file_result.class, metaDataMap);
		}

		public rename_poi_file_result() {
		}

		public rename_poi_file_result(boolean success) {
			this();
			this.success = success;
			setSuccessIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public rename_poi_file_result(rename_poi_file_result other) {
			__isset_bitfield = other.__isset_bitfield;
			this.success = other.success;
		}

		public rename_poi_file_result deepCopy() {
			return new rename_poi_file_result(this);
		}

		@Override
		public void clear() {
			setSuccessIsSet(false);
			this.success = false;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public rename_poi_file_result setSuccess(boolean success) {
			this.success = success;
			setSuccessIsSet(true);
			return this;
		}

		public void unsetSuccess() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return EncodingUtils.testBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		public void setSuccessIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SUCCESS_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Boolean) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return Boolean.valueOf(isSuccess());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof rename_poi_file_result)
				return this.equals((rename_poi_file_result) that);
			return false;
		}

		public boolean equals(rename_poi_file_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true;
			boolean that_present_success = true;
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (this.success != that.success)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(rename_poi_file_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("rename_poi_file_result(");
			boolean first = true;

			sb.append("success:");
			sb.append(this.success);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class rename_poi_file_resultStandardSchemeFactory implements SchemeFactory {
			public rename_poi_file_resultStandardScheme getScheme() {
				return new rename_poi_file_resultStandardScheme();
			}
		}

		private static class rename_poi_file_resultStandardScheme extends StandardScheme<rename_poi_file_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, rename_poi_file_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.success = iprot.readBool();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, rename_poi_file_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.isSetSuccess()) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeBool(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class rename_poi_file_resultTupleSchemeFactory implements SchemeFactory {
			public rename_poi_file_resultTupleScheme getScheme() {
				return new rename_poi_file_resultTupleScheme();
			}
		}

		private static class rename_poi_file_resultTupleScheme extends TupleScheme<rename_poi_file_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, rename_poi_file_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeBool(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, rename_poi_file_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readBool();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class rename_poi_args implements org.apache.thrift.TBase<rename_poi_args, rename_poi_args._Fields>, java.io.Serializable, Cloneable, Comparable<rename_poi_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("rename_poi_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 2);
		private static final org.apache.thrift.protocol.TField POI_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("poiName", org.apache.thrift.protocol.TType.STRING, (short) 3);
		private static final org.apache.thrift.protocol.TField NEW_POINAME_FIELD_DESC = new org.apache.thrift.protocol.TField("newPOIName", org.apache.thrift.protocol.TType.STRING, (short) 4);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new rename_poi_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new rename_poi_argsTupleSchemeFactory());
		}

		public String token; // required
		public String tripPath; // required
		public String poiName; // required
		public String newPOIName; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIP_PATH((short) 2, "tripPath"), POI_NAME((short) 3, "poiName"), NEW_POINAME((short) 4, "newPOIName");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIP_PATH
					return TRIP_PATH;
				case 3: // POI_NAME
					return POI_NAME;
				case 4: // NEW_POINAME
					return NEW_POINAME;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.POI_NAME, new org.apache.thrift.meta_data.FieldMetaData("poiName", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.NEW_POINAME, new org.apache.thrift.meta_data.FieldMetaData("newPOIName", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(rename_poi_args.class, metaDataMap);
		}

		public rename_poi_args() {
		}

		public rename_poi_args(String token, String tripPath, String poiName, String newPOIName) {
			this();
			this.token = token;
			this.tripPath = tripPath;
			this.poiName = poiName;
			this.newPOIName = newPOIName;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public rename_poi_args(rename_poi_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
			if (other.isSetPoiName()) {
				this.poiName = other.poiName;
			}
			if (other.isSetNewPOIName()) {
				this.newPOIName = other.newPOIName;
			}
		}

		public rename_poi_args deepCopy() {
			return new rename_poi_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.tripPath = null;
			this.poiName = null;
			this.newPOIName = null;
		}

		public String getToken() {
			return this.token;
		}

		public rename_poi_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public rename_poi_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public String getPoiName() {
			return this.poiName;
		}

		public rename_poi_args setPoiName(String poiName) {
			this.poiName = poiName;
			return this;
		}

		public void unsetPoiName() {
			this.poiName = null;
		}

		/**
		 * Returns true if field poiName is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetPoiName() {
			return this.poiName != null;
		}

		public void setPoiNameIsSet(boolean value) {
			if (!value) {
				this.poiName = null;
			}
		}

		public String getNewPOIName() {
			return this.newPOIName;
		}

		public rename_poi_args setNewPOIName(String newPOIName) {
			this.newPOIName = newPOIName;
			return this;
		}

		public void unsetNewPOIName() {
			this.newPOIName = null;
		}

		/**
		 * Returns true if field newPOIName is set (has been assigned a value)
		 * and false otherwise
		 */
		public boolean isSetNewPOIName() {
			return this.newPOIName != null;
		}

		public void setNewPOINameIsSet(boolean value) {
			if (!value) {
				this.newPOIName = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			case POI_NAME:
				if (value == null) {
					unsetPoiName();
				} else {
					setPoiName((String) value);
				}
				break;

			case NEW_POINAME:
				if (value == null) {
					unsetNewPOIName();
				} else {
					setNewPOIName((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIP_PATH:
				return getTripPath();

			case POI_NAME:
				return getPoiName();

			case NEW_POINAME:
				return getNewPOIName();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIP_PATH:
				return isSetTripPath();
			case POI_NAME:
				return isSetPoiName();
			case NEW_POINAME:
				return isSetNewPOIName();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof rename_poi_args)
				return this.equals((rename_poi_args) that);
			return false;
		}

		public boolean equals(rename_poi_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			boolean this_present_poiName = true && this.isSetPoiName();
			boolean that_present_poiName = true && that.isSetPoiName();
			if (this_present_poiName || that_present_poiName) {
				if (!(this_present_poiName && that_present_poiName))
					return false;
				if (!this.poiName.equals(that.poiName))
					return false;
			}

			boolean this_present_newPOIName = true && this.isSetNewPOIName();
			boolean that_present_newPOIName = true && that.isSetNewPOIName();
			if (this_present_newPOIName || that_present_newPOIName) {
				if (!(this_present_newPOIName && that_present_newPOIName))
					return false;
				if (!this.newPOIName.equals(that.newPOIName))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(rename_poi_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetPoiName()).compareTo(other.isSetPoiName());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetPoiName()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.poiName, other.poiName);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetNewPOIName()).compareTo(other.isSetNewPOIName());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetNewPOIName()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.newPOIName, other.newPOIName);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("rename_poi_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("poiName:");
			if (this.poiName == null) {
				sb.append("null");
			} else {
				sb.append(this.poiName);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("newPOIName:");
			if (this.newPOIName == null) {
				sb.append("null");
			} else {
				sb.append(this.newPOIName);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class rename_poi_argsStandardSchemeFactory implements SchemeFactory {
			public rename_poi_argsStandardScheme getScheme() {
				return new rename_poi_argsStandardScheme();
			}
		}

		private static class rename_poi_argsStandardScheme extends StandardScheme<rename_poi_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, rename_poi_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 3: // POI_NAME
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.poiName = iprot.readString();
							struct.setPoiNameIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 4: // NEW_POINAME
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.newPOIName = iprot.readString();
							struct.setNewPOINameIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, rename_poi_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				if (struct.poiName != null) {
					oprot.writeFieldBegin(POI_NAME_FIELD_DESC);
					oprot.writeString(struct.poiName);
					oprot.writeFieldEnd();
				}
				if (struct.newPOIName != null) {
					oprot.writeFieldBegin(NEW_POINAME_FIELD_DESC);
					oprot.writeString(struct.newPOIName);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class rename_poi_argsTupleSchemeFactory implements SchemeFactory {
			public rename_poi_argsTupleScheme getScheme() {
				return new rename_poi_argsTupleScheme();
			}
		}

		private static class rename_poi_argsTupleScheme extends TupleScheme<rename_poi_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, rename_poi_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTripPath()) {
					optionals.set(1);
				}
				if (struct.isSetPoiName()) {
					optionals.set(2);
				}
				if (struct.isSetNewPOIName()) {
					optionals.set(3);
				}
				oprot.writeBitSet(optionals, 4);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
				if (struct.isSetPoiName()) {
					oprot.writeString(struct.poiName);
				}
				if (struct.isSetNewPOIName()) {
					oprot.writeString(struct.newPOIName);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, rename_poi_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(4);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
				if (incoming.get(2)) {
					struct.poiName = iprot.readString();
					struct.setPoiNameIsSet(true);
				}
				if (incoming.get(3)) {
					struct.newPOIName = iprot.readString();
					struct.setNewPOINameIsSet(true);
				}
			}
		}

	}

	public static class rename_poi_result implements org.apache.thrift.TBase<rename_poi_result, rename_poi_result._Fields>, java.io.Serializable, Cloneable, Comparable<rename_poi_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("rename_poi_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.BOOL, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new rename_poi_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new rename_poi_resultTupleSchemeFactory());
		}

		public boolean success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __SUCCESS_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(rename_poi_result.class, metaDataMap);
		}

		public rename_poi_result() {
		}

		public rename_poi_result(boolean success) {
			this();
			this.success = success;
			setSuccessIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public rename_poi_result(rename_poi_result other) {
			__isset_bitfield = other.__isset_bitfield;
			this.success = other.success;
		}

		public rename_poi_result deepCopy() {
			return new rename_poi_result(this);
		}

		@Override
		public void clear() {
			setSuccessIsSet(false);
			this.success = false;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public rename_poi_result setSuccess(boolean success) {
			this.success = success;
			setSuccessIsSet(true);
			return this;
		}

		public void unsetSuccess() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return EncodingUtils.testBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		public void setSuccessIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SUCCESS_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Boolean) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return Boolean.valueOf(isSuccess());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof rename_poi_result)
				return this.equals((rename_poi_result) that);
			return false;
		}

		public boolean equals(rename_poi_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true;
			boolean that_present_success = true;
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (this.success != that.success)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(rename_poi_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("rename_poi_result(");
			boolean first = true;

			sb.append("success:");
			sb.append(this.success);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class rename_poi_resultStandardSchemeFactory implements SchemeFactory {
			public rename_poi_resultStandardScheme getScheme() {
				return new rename_poi_resultStandardScheme();
			}
		}

		private static class rename_poi_resultStandardScheme extends StandardScheme<rename_poi_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, rename_poi_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.success = iprot.readBool();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, rename_poi_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.isSetSuccess()) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeBool(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class rename_poi_resultTupleSchemeFactory implements SchemeFactory {
			public rename_poi_resultTupleScheme getScheme() {
				return new rename_poi_resultTupleScheme();
			}
		}

		private static class rename_poi_resultTupleScheme extends TupleScheme<rename_poi_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, rename_poi_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeBool(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, rename_poi_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readBool();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class rename_trip_args implements org.apache.thrift.TBase<rename_trip_args, rename_trip_args._Fields>, java.io.Serializable, Cloneable, Comparable<rename_trip_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("rename_trip_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIPPATH_FIELD_DESC = new org.apache.thrift.protocol.TField("trippath", org.apache.thrift.protocol.TType.STRING, (short) 2);
		private static final org.apache.thrift.protocol.TField NEW_TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("newTripPath", org.apache.thrift.protocol.TType.STRING, (short) 3);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new rename_trip_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new rename_trip_argsTupleSchemeFactory());
		}

		public String token; // required
		public String trippath; // required
		public String newTripPath; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIPPATH((short) 2, "trippath"), NEW_TRIP_PATH((short) 3, "newTripPath");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIPPATH
					return TRIPPATH;
				case 3: // NEW_TRIP_PATH
					return NEW_TRIP_PATH;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIPPATH, new org.apache.thrift.meta_data.FieldMetaData("trippath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.NEW_TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("newTripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(rename_trip_args.class, metaDataMap);
		}

		public rename_trip_args() {
		}

		public rename_trip_args(String token, String trippath, String newTripPath) {
			this();
			this.token = token;
			this.trippath = trippath;
			this.newTripPath = newTripPath;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public rename_trip_args(rename_trip_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTrippath()) {
				this.trippath = other.trippath;
			}
			if (other.isSetNewTripPath()) {
				this.newTripPath = other.newTripPath;
			}
		}

		public rename_trip_args deepCopy() {
			return new rename_trip_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.trippath = null;
			this.newTripPath = null;
		}

		public String getToken() {
			return this.token;
		}

		public rename_trip_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTrippath() {
			return this.trippath;
		}

		public rename_trip_args setTrippath(String trippath) {
			this.trippath = trippath;
			return this;
		}

		public void unsetTrippath() {
			this.trippath = null;
		}

		/**
		 * Returns true if field trippath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTrippath() {
			return this.trippath != null;
		}

		public void setTrippathIsSet(boolean value) {
			if (!value) {
				this.trippath = null;
			}
		}

		public String getNewTripPath() {
			return this.newTripPath;
		}

		public rename_trip_args setNewTripPath(String newTripPath) {
			this.newTripPath = newTripPath;
			return this;
		}

		public void unsetNewTripPath() {
			this.newTripPath = null;
		}

		/**
		 * Returns true if field newTripPath is set (has been assigned a value)
		 * and false otherwise
		 */
		public boolean isSetNewTripPath() {
			return this.newTripPath != null;
		}

		public void setNewTripPathIsSet(boolean value) {
			if (!value) {
				this.newTripPath = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIPPATH:
				if (value == null) {
					unsetTrippath();
				} else {
					setTrippath((String) value);
				}
				break;

			case NEW_TRIP_PATH:
				if (value == null) {
					unsetNewTripPath();
				} else {
					setNewTripPath((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIPPATH:
				return getTrippath();

			case NEW_TRIP_PATH:
				return getNewTripPath();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIPPATH:
				return isSetTrippath();
			case NEW_TRIP_PATH:
				return isSetNewTripPath();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof rename_trip_args)
				return this.equals((rename_trip_args) that);
			return false;
		}

		public boolean equals(rename_trip_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_trippath = true && this.isSetTrippath();
			boolean that_present_trippath = true && that.isSetTrippath();
			if (this_present_trippath || that_present_trippath) {
				if (!(this_present_trippath && that_present_trippath))
					return false;
				if (!this.trippath.equals(that.trippath))
					return false;
			}

			boolean this_present_newTripPath = true && this.isSetNewTripPath();
			boolean that_present_newTripPath = true && that.isSetNewTripPath();
			if (this_present_newTripPath || that_present_newTripPath) {
				if (!(this_present_newTripPath && that_present_newTripPath))
					return false;
				if (!this.newTripPath.equals(that.newTripPath))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(rename_trip_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTrippath()).compareTo(other.isSetTrippath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTrippath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.trippath, other.trippath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetNewTripPath()).compareTo(other.isSetNewTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetNewTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.newTripPath, other.newTripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("rename_trip_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("trippath:");
			if (this.trippath == null) {
				sb.append("null");
			} else {
				sb.append(this.trippath);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("newTripPath:");
			if (this.newTripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.newTripPath);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class rename_trip_argsStandardSchemeFactory implements SchemeFactory {
			public rename_trip_argsStandardScheme getScheme() {
				return new rename_trip_argsStandardScheme();
			}
		}

		private static class rename_trip_argsStandardScheme extends StandardScheme<rename_trip_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, rename_trip_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIPPATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.trippath = iprot.readString();
							struct.setTrippathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 3: // NEW_TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.newTripPath = iprot.readString();
							struct.setNewTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, rename_trip_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.trippath != null) {
					oprot.writeFieldBegin(TRIPPATH_FIELD_DESC);
					oprot.writeString(struct.trippath);
					oprot.writeFieldEnd();
				}
				if (struct.newTripPath != null) {
					oprot.writeFieldBegin(NEW_TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.newTripPath);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class rename_trip_argsTupleSchemeFactory implements SchemeFactory {
			public rename_trip_argsTupleScheme getScheme() {
				return new rename_trip_argsTupleScheme();
			}
		}

		private static class rename_trip_argsTupleScheme extends TupleScheme<rename_trip_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, rename_trip_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTrippath()) {
					optionals.set(1);
				}
				if (struct.isSetNewTripPath()) {
					optionals.set(2);
				}
				oprot.writeBitSet(optionals, 3);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTrippath()) {
					oprot.writeString(struct.trippath);
				}
				if (struct.isSetNewTripPath()) {
					oprot.writeString(struct.newTripPath);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, rename_trip_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(3);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.trippath = iprot.readString();
					struct.setTrippathIsSet(true);
				}
				if (incoming.get(2)) {
					struct.newTripPath = iprot.readString();
					struct.setNewTripPathIsSet(true);
				}
			}
		}

	}

	public static class rename_trip_result implements org.apache.thrift.TBase<rename_trip_result, rename_trip_result._Fields>, java.io.Serializable, Cloneable, Comparable<rename_trip_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("rename_trip_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.BOOL, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new rename_trip_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new rename_trip_resultTupleSchemeFactory());
		}

		public boolean success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __SUCCESS_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(rename_trip_result.class, metaDataMap);
		}

		public rename_trip_result() {
		}

		public rename_trip_result(boolean success) {
			this();
			this.success = success;
			setSuccessIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public rename_trip_result(rename_trip_result other) {
			__isset_bitfield = other.__isset_bitfield;
			this.success = other.success;
		}

		public rename_trip_result deepCopy() {
			return new rename_trip_result(this);
		}

		@Override
		public void clear() {
			setSuccessIsSet(false);
			this.success = false;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public rename_trip_result setSuccess(boolean success) {
			this.success = success;
			setSuccessIsSet(true);
			return this;
		}

		public void unsetSuccess() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return EncodingUtils.testBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		public void setSuccessIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SUCCESS_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Boolean) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return Boolean.valueOf(isSuccess());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof rename_trip_result)
				return this.equals((rename_trip_result) that);
			return false;
		}

		public boolean equals(rename_trip_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true;
			boolean that_present_success = true;
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (this.success != that.success)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(rename_trip_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("rename_trip_result(");
			boolean first = true;

			sb.append("success:");
			sb.append(this.success);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class rename_trip_resultStandardSchemeFactory implements SchemeFactory {
			public rename_trip_resultStandardScheme getScheme() {
				return new rename_trip_resultStandardScheme();
			}
		}

		private static class rename_trip_resultStandardScheme extends StandardScheme<rename_trip_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, rename_trip_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.success = iprot.readBool();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, rename_trip_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.isSetSuccess()) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeBool(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class rename_trip_resultTupleSchemeFactory implements SchemeFactory {
			public rename_trip_resultTupleScheme getScheme() {
				return new rename_trip_resultTupleScheme();
			}
		}

		private static class rename_trip_resultTupleScheme extends TupleScheme<rename_trip_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, rename_trip_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeBool(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, rename_trip_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readBool();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class add_poi_args implements org.apache.thrift.TBase<add_poi_args, add_poi_args._Fields>, java.io.Serializable, Cloneable, Comparable<add_poi_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("add_poi_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 2);
		private static final org.apache.thrift.protocol.TField POI_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("poiName", org.apache.thrift.protocol.TType.STRING, (short) 3);
		private static final org.apache.thrift.protocol.TField BASICINFORMATION_FIELD_DESC = new org.apache.thrift.protocol.TField("basicinformation", org.apache.thrift.protocol.TType.STRING, (short) 4);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new add_poi_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new add_poi_argsTupleSchemeFactory());
		}

		public String token; // required
		public String tripPath; // required
		public String poiName; // required
		public String basicinformation; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIP_PATH((short) 2, "tripPath"), POI_NAME((short) 3, "poiName"), BASICINFORMATION((short) 4, "basicinformation");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIP_PATH
					return TRIP_PATH;
				case 3: // POI_NAME
					return POI_NAME;
				case 4: // BASICINFORMATION
					return BASICINFORMATION;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.POI_NAME, new org.apache.thrift.meta_data.FieldMetaData("poiName", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.BASICINFORMATION, new org.apache.thrift.meta_data.FieldMetaData("basicinformation", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(add_poi_args.class, metaDataMap);
		}

		public add_poi_args() {
		}

		public add_poi_args(String token, String tripPath, String poiName, String basicinformation) {
			this();
			this.token = token;
			this.tripPath = tripPath;
			this.poiName = poiName;
			this.basicinformation = basicinformation;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public add_poi_args(add_poi_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
			if (other.isSetPoiName()) {
				this.poiName = other.poiName;
			}
			if (other.isSetBasicinformation()) {
				this.basicinformation = other.basicinformation;
			}
		}

		public add_poi_args deepCopy() {
			return new add_poi_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.tripPath = null;
			this.poiName = null;
			this.basicinformation = null;
		}

		public String getToken() {
			return this.token;
		}

		public add_poi_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public add_poi_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public String getPoiName() {
			return this.poiName;
		}

		public add_poi_args setPoiName(String poiName) {
			this.poiName = poiName;
			return this;
		}

		public void unsetPoiName() {
			this.poiName = null;
		}

		/**
		 * Returns true if field poiName is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetPoiName() {
			return this.poiName != null;
		}

		public void setPoiNameIsSet(boolean value) {
			if (!value) {
				this.poiName = null;
			}
		}

		public String getBasicinformation() {
			return this.basicinformation;
		}

		public add_poi_args setBasicinformation(String basicinformation) {
			this.basicinformation = basicinformation;
			return this;
		}

		public void unsetBasicinformation() {
			this.basicinformation = null;
		}

		/**
		 * Returns true if field basicinformation is set (has been assigned a
		 * value) and false otherwise
		 */
		public boolean isSetBasicinformation() {
			return this.basicinformation != null;
		}

		public void setBasicinformationIsSet(boolean value) {
			if (!value) {
				this.basicinformation = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			case POI_NAME:
				if (value == null) {
					unsetPoiName();
				} else {
					setPoiName((String) value);
				}
				break;

			case BASICINFORMATION:
				if (value == null) {
					unsetBasicinformation();
				} else {
					setBasicinformation((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIP_PATH:
				return getTripPath();

			case POI_NAME:
				return getPoiName();

			case BASICINFORMATION:
				return getBasicinformation();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIP_PATH:
				return isSetTripPath();
			case POI_NAME:
				return isSetPoiName();
			case BASICINFORMATION:
				return isSetBasicinformation();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof add_poi_args)
				return this.equals((add_poi_args) that);
			return false;
		}

		public boolean equals(add_poi_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			boolean this_present_poiName = true && this.isSetPoiName();
			boolean that_present_poiName = true && that.isSetPoiName();
			if (this_present_poiName || that_present_poiName) {
				if (!(this_present_poiName && that_present_poiName))
					return false;
				if (!this.poiName.equals(that.poiName))
					return false;
			}

			boolean this_present_basicinformation = true && this.isSetBasicinformation();
			boolean that_present_basicinformation = true && that.isSetBasicinformation();
			if (this_present_basicinformation || that_present_basicinformation) {
				if (!(this_present_basicinformation && that_present_basicinformation))
					return false;
				if (!this.basicinformation.equals(that.basicinformation))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(add_poi_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetPoiName()).compareTo(other.isSetPoiName());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetPoiName()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.poiName, other.poiName);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetBasicinformation()).compareTo(other.isSetBasicinformation());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetBasicinformation()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.basicinformation, other.basicinformation);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("add_poi_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("poiName:");
			if (this.poiName == null) {
				sb.append("null");
			} else {
				sb.append(this.poiName);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("basicinformation:");
			if (this.basicinformation == null) {
				sb.append("null");
			} else {
				sb.append(this.basicinformation);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class add_poi_argsStandardSchemeFactory implements SchemeFactory {
			public add_poi_argsStandardScheme getScheme() {
				return new add_poi_argsStandardScheme();
			}
		}

		private static class add_poi_argsStandardScheme extends StandardScheme<add_poi_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, add_poi_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 3: // POI_NAME
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.poiName = iprot.readString();
							struct.setPoiNameIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 4: // BASICINFORMATION
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.basicinformation = iprot.readString();
							struct.setBasicinformationIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, add_poi_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				if (struct.poiName != null) {
					oprot.writeFieldBegin(POI_NAME_FIELD_DESC);
					oprot.writeString(struct.poiName);
					oprot.writeFieldEnd();
				}
				if (struct.basicinformation != null) {
					oprot.writeFieldBegin(BASICINFORMATION_FIELD_DESC);
					oprot.writeString(struct.basicinformation);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class add_poi_argsTupleSchemeFactory implements SchemeFactory {
			public add_poi_argsTupleScheme getScheme() {
				return new add_poi_argsTupleScheme();
			}
		}

		private static class add_poi_argsTupleScheme extends TupleScheme<add_poi_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, add_poi_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTripPath()) {
					optionals.set(1);
				}
				if (struct.isSetPoiName()) {
					optionals.set(2);
				}
				if (struct.isSetBasicinformation()) {
					optionals.set(3);
				}
				oprot.writeBitSet(optionals, 4);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
				if (struct.isSetPoiName()) {
					oprot.writeString(struct.poiName);
				}
				if (struct.isSetBasicinformation()) {
					oprot.writeString(struct.basicinformation);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, add_poi_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(4);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
				if (incoming.get(2)) {
					struct.poiName = iprot.readString();
					struct.setPoiNameIsSet(true);
				}
				if (incoming.get(3)) {
					struct.basicinformation = iprot.readString();
					struct.setBasicinformationIsSet(true);
				}
			}
		}

	}

	public static class add_poi_result implements org.apache.thrift.TBase<add_poi_result, add_poi_result._Fields>, java.io.Serializable, Cloneable, Comparable<add_poi_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("add_poi_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.BOOL, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new add_poi_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new add_poi_resultTupleSchemeFactory());
		}

		public boolean success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __SUCCESS_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(add_poi_result.class, metaDataMap);
		}

		public add_poi_result() {
		}

		public add_poi_result(boolean success) {
			this();
			this.success = success;
			setSuccessIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public add_poi_result(add_poi_result other) {
			__isset_bitfield = other.__isset_bitfield;
			this.success = other.success;
		}

		public add_poi_result deepCopy() {
			return new add_poi_result(this);
		}

		@Override
		public void clear() {
			setSuccessIsSet(false);
			this.success = false;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public add_poi_result setSuccess(boolean success) {
			this.success = success;
			setSuccessIsSet(true);
			return this;
		}

		public void unsetSuccess() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return EncodingUtils.testBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		public void setSuccessIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SUCCESS_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Boolean) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return Boolean.valueOf(isSuccess());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof add_poi_result)
				return this.equals((add_poi_result) that);
			return false;
		}

		public boolean equals(add_poi_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true;
			boolean that_present_success = true;
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (this.success != that.success)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(add_poi_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("add_poi_result(");
			boolean first = true;

			sb.append("success:");
			sb.append(this.success);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class add_poi_resultStandardSchemeFactory implements SchemeFactory {
			public add_poi_resultStandardScheme getScheme() {
				return new add_poi_resultStandardScheme();
			}
		}

		private static class add_poi_resultStandardScheme extends StandardScheme<add_poi_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, add_poi_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.success = iprot.readBool();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, add_poi_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.isSetSuccess()) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeBool(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class add_poi_resultTupleSchemeFactory implements SchemeFactory {
			public add_poi_resultTupleScheme getScheme() {
				return new add_poi_resultTupleScheme();
			}
		}

		private static class add_poi_resultTupleScheme extends TupleScheme<add_poi_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, add_poi_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeBool(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, add_poi_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readBool();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class toggle_public_args implements org.apache.thrift.TBase<toggle_public_args, toggle_public_args._Fields>, java.io.Serializable, Cloneable, Comparable<toggle_public_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("toggle_public_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 2);
		private static final org.apache.thrift.protocol.TField OPTION_FIELD_DESC = new org.apache.thrift.protocol.TField("option", org.apache.thrift.protocol.TType.STRING, (short) 3);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new toggle_public_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new toggle_public_argsTupleSchemeFactory());
		}

		public String token; // required
		public String tripPath; // required
		public String option; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIP_PATH((short) 2, "tripPath"), OPTION((short) 3, "option");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIP_PATH
					return TRIP_PATH;
				case 3: // OPTION
					return OPTION;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.OPTION, new org.apache.thrift.meta_data.FieldMetaData("option", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(toggle_public_args.class, metaDataMap);
		}

		public toggle_public_args() {
		}

		public toggle_public_args(String token, String tripPath, String option) {
			this();
			this.token = token;
			this.tripPath = tripPath;
			this.option = option;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public toggle_public_args(toggle_public_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
			if (other.isSetOption()) {
				this.option = other.option;
			}
		}

		public toggle_public_args deepCopy() {
			return new toggle_public_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.tripPath = null;
			this.option = null;
		}

		public String getToken() {
			return this.token;
		}

		public toggle_public_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public toggle_public_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public String getOption() {
			return this.option;
		}

		public toggle_public_args setOption(String option) {
			this.option = option;
			return this;
		}

		public void unsetOption() {
			this.option = null;
		}

		/**
		 * Returns true if field option is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetOption() {
			return this.option != null;
		}

		public void setOptionIsSet(boolean value) {
			if (!value) {
				this.option = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			case OPTION:
				if (value == null) {
					unsetOption();
				} else {
					setOption((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIP_PATH:
				return getTripPath();

			case OPTION:
				return getOption();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIP_PATH:
				return isSetTripPath();
			case OPTION:
				return isSetOption();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof toggle_public_args)
				return this.equals((toggle_public_args) that);
			return false;
		}

		public boolean equals(toggle_public_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			boolean this_present_option = true && this.isSetOption();
			boolean that_present_option = true && that.isSetOption();
			if (this_present_option || that_present_option) {
				if (!(this_present_option && that_present_option))
					return false;
				if (!this.option.equals(that.option))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(toggle_public_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetOption()).compareTo(other.isSetOption());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetOption()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.option, other.option);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("toggle_public_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("option:");
			if (this.option == null) {
				sb.append("null");
			} else {
				sb.append(this.option);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class toggle_public_argsStandardSchemeFactory implements SchemeFactory {
			public toggle_public_argsStandardScheme getScheme() {
				return new toggle_public_argsStandardScheme();
			}
		}

		private static class toggle_public_argsStandardScheme extends StandardScheme<toggle_public_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, toggle_public_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 3: // OPTION
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.option = iprot.readString();
							struct.setOptionIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, toggle_public_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				if (struct.option != null) {
					oprot.writeFieldBegin(OPTION_FIELD_DESC);
					oprot.writeString(struct.option);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class toggle_public_argsTupleSchemeFactory implements SchemeFactory {
			public toggle_public_argsTupleScheme getScheme() {
				return new toggle_public_argsTupleScheme();
			}
		}

		private static class toggle_public_argsTupleScheme extends TupleScheme<toggle_public_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, toggle_public_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTripPath()) {
					optionals.set(1);
				}
				if (struct.isSetOption()) {
					optionals.set(2);
				}
				oprot.writeBitSet(optionals, 3);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
				if (struct.isSetOption()) {
					oprot.writeString(struct.option);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, toggle_public_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(3);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
				if (incoming.get(2)) {
					struct.option = iprot.readString();
					struct.setOptionIsSet(true);
				}
			}
		}

	}

	public static class toggle_public_result implements org.apache.thrift.TBase<toggle_public_result, toggle_public_result._Fields>, java.io.Serializable, Cloneable, Comparable<toggle_public_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("toggle_public_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.BOOL, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new toggle_public_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new toggle_public_resultTupleSchemeFactory());
		}

		public boolean success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __SUCCESS_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(toggle_public_result.class, metaDataMap);
		}

		public toggle_public_result() {
		}

		public toggle_public_result(boolean success) {
			this();
			this.success = success;
			setSuccessIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public toggle_public_result(toggle_public_result other) {
			__isset_bitfield = other.__isset_bitfield;
			this.success = other.success;
		}

		public toggle_public_result deepCopy() {
			return new toggle_public_result(this);
		}

		@Override
		public void clear() {
			setSuccessIsSet(false);
			this.success = false;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public toggle_public_result setSuccess(boolean success) {
			this.success = success;
			setSuccessIsSet(true);
			return this;
		}

		public void unsetSuccess() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return EncodingUtils.testBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		public void setSuccessIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SUCCESS_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Boolean) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return Boolean.valueOf(isSuccess());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof toggle_public_result)
				return this.equals((toggle_public_result) that);
			return false;
		}

		public boolean equals(toggle_public_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true;
			boolean that_present_success = true;
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (this.success != that.success)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(toggle_public_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("toggle_public_result(");
			boolean first = true;

			sb.append("success:");
			sb.append(this.success);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class toggle_public_resultStandardSchemeFactory implements SchemeFactory {
			public toggle_public_resultStandardScheme getScheme() {
				return new toggle_public_resultStandardScheme();
			}
		}

		private static class toggle_public_resultStandardScheme extends StandardScheme<toggle_public_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, toggle_public_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.success = iprot.readBool();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, toggle_public_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.isSetSuccess()) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeBool(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class toggle_public_resultTupleSchemeFactory implements SchemeFactory {
			public toggle_public_resultTupleScheme getScheme() {
				return new toggle_public_resultTupleScheme();
			}
		}

		private static class toggle_public_resultTupleScheme extends TupleScheme<toggle_public_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, toggle_public_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeBool(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, toggle_public_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readBool();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class add_like_args implements org.apache.thrift.TBase<add_like_args, add_like_args._Fields>, java.io.Serializable, Cloneable, Comparable<add_like_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("add_like_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 2);
		private static final org.apache.thrift.protocol.TField WHO_FIELD_DESC = new org.apache.thrift.protocol.TField("who", org.apache.thrift.protocol.TType.STRING, (short) 3);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new add_like_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new add_like_argsTupleSchemeFactory());
		}

		public String token; // required
		public String tripPath; // required
		public String who; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIP_PATH((short) 2, "tripPath"), WHO((short) 3, "who");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIP_PATH
					return TRIP_PATH;
				case 3: // WHO
					return WHO;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.WHO, new org.apache.thrift.meta_data.FieldMetaData("who", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(add_like_args.class, metaDataMap);
		}

		public add_like_args() {
		}

		public add_like_args(String token, String tripPath, String who) {
			this();
			this.token = token;
			this.tripPath = tripPath;
			this.who = who;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public add_like_args(add_like_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
			if (other.isSetWho()) {
				this.who = other.who;
			}
		}

		public add_like_args deepCopy() {
			return new add_like_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.tripPath = null;
			this.who = null;
		}

		public String getToken() {
			return this.token;
		}

		public add_like_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public add_like_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public String getWho() {
			return this.who;
		}

		public add_like_args setWho(String who) {
			this.who = who;
			return this;
		}

		public void unsetWho() {
			this.who = null;
		}

		/**
		 * Returns true if field who is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetWho() {
			return this.who != null;
		}

		public void setWhoIsSet(boolean value) {
			if (!value) {
				this.who = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			case WHO:
				if (value == null) {
					unsetWho();
				} else {
					setWho((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIP_PATH:
				return getTripPath();

			case WHO:
				return getWho();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIP_PATH:
				return isSetTripPath();
			case WHO:
				return isSetWho();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof add_like_args)
				return this.equals((add_like_args) that);
			return false;
		}

		public boolean equals(add_like_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			boolean this_present_who = true && this.isSetWho();
			boolean that_present_who = true && that.isSetWho();
			if (this_present_who || that_present_who) {
				if (!(this_present_who && that_present_who))
					return false;
				if (!this.who.equals(that.who))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(add_like_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetWho()).compareTo(other.isSetWho());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetWho()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.who, other.who);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("add_like_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("who:");
			if (this.who == null) {
				sb.append("null");
			} else {
				sb.append(this.who);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class add_like_argsStandardSchemeFactory implements SchemeFactory {
			public add_like_argsStandardScheme getScheme() {
				return new add_like_argsStandardScheme();
			}
		}

		private static class add_like_argsStandardScheme extends StandardScheme<add_like_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, add_like_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 3: // WHO
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.who = iprot.readString();
							struct.setWhoIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, add_like_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				if (struct.who != null) {
					oprot.writeFieldBegin(WHO_FIELD_DESC);
					oprot.writeString(struct.who);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class add_like_argsTupleSchemeFactory implements SchemeFactory {
			public add_like_argsTupleScheme getScheme() {
				return new add_like_argsTupleScheme();
			}
		}

		private static class add_like_argsTupleScheme extends TupleScheme<add_like_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, add_like_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTripPath()) {
					optionals.set(1);
				}
				if (struct.isSetWho()) {
					optionals.set(2);
				}
				oprot.writeBitSet(optionals, 3);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
				if (struct.isSetWho()) {
					oprot.writeString(struct.who);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, add_like_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(3);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
				if (incoming.get(2)) {
					struct.who = iprot.readString();
					struct.setWhoIsSet(true);
				}
			}
		}

	}

	public static class add_like_result implements org.apache.thrift.TBase<add_like_result, add_like_result._Fields>, java.io.Serializable, Cloneable, Comparable<add_like_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("add_like_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.BOOL, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new add_like_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new add_like_resultTupleSchemeFactory());
		}

		public boolean success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __SUCCESS_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(add_like_result.class, metaDataMap);
		}

		public add_like_result() {
		}

		public add_like_result(boolean success) {
			this();
			this.success = success;
			setSuccessIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public add_like_result(add_like_result other) {
			__isset_bitfield = other.__isset_bitfield;
			this.success = other.success;
		}

		public add_like_result deepCopy() {
			return new add_like_result(this);
		}

		@Override
		public void clear() {
			setSuccessIsSet(false);
			this.success = false;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public add_like_result setSuccess(boolean success) {
			this.success = success;
			setSuccessIsSet(true);
			return this;
		}

		public void unsetSuccess() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return EncodingUtils.testBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		public void setSuccessIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SUCCESS_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Boolean) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return Boolean.valueOf(isSuccess());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof add_like_result)
				return this.equals((add_like_result) that);
			return false;
		}

		public boolean equals(add_like_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true;
			boolean that_present_success = true;
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (this.success != that.success)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(add_like_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("add_like_result(");
			boolean first = true;

			sb.append("success:");
			sb.append(this.success);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class add_like_resultStandardSchemeFactory implements SchemeFactory {
			public add_like_resultStandardScheme getScheme() {
				return new add_like_resultStandardScheme();
			}
		}

		private static class add_like_resultStandardScheme extends StandardScheme<add_like_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, add_like_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.success = iprot.readBool();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, add_like_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.isSetSuccess()) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeBool(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class add_like_resultTupleSchemeFactory implements SchemeFactory {
			public add_like_resultTupleScheme getScheme() {
				return new add_like_resultTupleScheme();
			}
		}

		private static class add_like_resultTupleScheme extends TupleScheme<add_like_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, add_like_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeBool(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, add_like_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readBool();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class delete_like_args implements org.apache.thrift.TBase<delete_like_args, delete_like_args._Fields>, java.io.Serializable, Cloneable, Comparable<delete_like_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("delete_like_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 2);
		private static final org.apache.thrift.protocol.TField WHO_FIELD_DESC = new org.apache.thrift.protocol.TField("who", org.apache.thrift.protocol.TType.STRING, (short) 3);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new delete_like_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new delete_like_argsTupleSchemeFactory());
		}

		public String token; // required
		public String tripPath; // required
		public String who; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIP_PATH((short) 2, "tripPath"), WHO((short) 3, "who");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIP_PATH
					return TRIP_PATH;
				case 3: // WHO
					return WHO;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.WHO, new org.apache.thrift.meta_data.FieldMetaData("who", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(delete_like_args.class, metaDataMap);
		}

		public delete_like_args() {
		}

		public delete_like_args(String token, String tripPath, String who) {
			this();
			this.token = token;
			this.tripPath = tripPath;
			this.who = who;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public delete_like_args(delete_like_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
			if (other.isSetWho()) {
				this.who = other.who;
			}
		}

		public delete_like_args deepCopy() {
			return new delete_like_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.tripPath = null;
			this.who = null;
		}

		public String getToken() {
			return this.token;
		}

		public delete_like_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public delete_like_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public String getWho() {
			return this.who;
		}

		public delete_like_args setWho(String who) {
			this.who = who;
			return this;
		}

		public void unsetWho() {
			this.who = null;
		}

		/**
		 * Returns true if field who is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetWho() {
			return this.who != null;
		}

		public void setWhoIsSet(boolean value) {
			if (!value) {
				this.who = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			case WHO:
				if (value == null) {
					unsetWho();
				} else {
					setWho((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIP_PATH:
				return getTripPath();

			case WHO:
				return getWho();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIP_PATH:
				return isSetTripPath();
			case WHO:
				return isSetWho();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof delete_like_args)
				return this.equals((delete_like_args) that);
			return false;
		}

		public boolean equals(delete_like_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			boolean this_present_who = true && this.isSetWho();
			boolean that_present_who = true && that.isSetWho();
			if (this_present_who || that_present_who) {
				if (!(this_present_who && that_present_who))
					return false;
				if (!this.who.equals(that.who))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(delete_like_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetWho()).compareTo(other.isSetWho());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetWho()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.who, other.who);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("delete_like_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("who:");
			if (this.who == null) {
				sb.append("null");
			} else {
				sb.append(this.who);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class delete_like_argsStandardSchemeFactory implements SchemeFactory {
			public delete_like_argsStandardScheme getScheme() {
				return new delete_like_argsStandardScheme();
			}
		}

		private static class delete_like_argsStandardScheme extends StandardScheme<delete_like_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, delete_like_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 3: // WHO
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.who = iprot.readString();
							struct.setWhoIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, delete_like_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				if (struct.who != null) {
					oprot.writeFieldBegin(WHO_FIELD_DESC);
					oprot.writeString(struct.who);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class delete_like_argsTupleSchemeFactory implements SchemeFactory {
			public delete_like_argsTupleScheme getScheme() {
				return new delete_like_argsTupleScheme();
			}
		}

		private static class delete_like_argsTupleScheme extends TupleScheme<delete_like_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, delete_like_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTripPath()) {
					optionals.set(1);
				}
				if (struct.isSetWho()) {
					optionals.set(2);
				}
				oprot.writeBitSet(optionals, 3);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
				if (struct.isSetWho()) {
					oprot.writeString(struct.who);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, delete_like_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(3);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
				if (incoming.get(2)) {
					struct.who = iprot.readString();
					struct.setWhoIsSet(true);
				}
			}
		}

	}

	public static class delete_like_result implements org.apache.thrift.TBase<delete_like_result, delete_like_result._Fields>, java.io.Serializable, Cloneable, Comparable<delete_like_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("delete_like_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.BOOL, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new delete_like_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new delete_like_resultTupleSchemeFactory());
		}

		public boolean success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __SUCCESS_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(delete_like_result.class, metaDataMap);
		}

		public delete_like_result() {
		}

		public delete_like_result(boolean success) {
			this();
			this.success = success;
			setSuccessIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public delete_like_result(delete_like_result other) {
			__isset_bitfield = other.__isset_bitfield;
			this.success = other.success;
		}

		public delete_like_result deepCopy() {
			return new delete_like_result(this);
		}

		@Override
		public void clear() {
			setSuccessIsSet(false);
			this.success = false;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public delete_like_result setSuccess(boolean success) {
			this.success = success;
			setSuccessIsSet(true);
			return this;
		}

		public void unsetSuccess() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return EncodingUtils.testBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		public void setSuccessIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SUCCESS_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Boolean) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return Boolean.valueOf(isSuccess());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof delete_like_result)
				return this.equals((delete_like_result) that);
			return false;
		}

		public boolean equals(delete_like_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true;
			boolean that_present_success = true;
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (this.success != that.success)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(delete_like_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("delete_like_result(");
			boolean first = true;

			sb.append("success:");
			sb.append(this.success);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class delete_like_resultStandardSchemeFactory implements SchemeFactory {
			public delete_like_resultStandardScheme getScheme() {
				return new delete_like_resultStandardScheme();
			}
		}

		private static class delete_like_resultStandardScheme extends StandardScheme<delete_like_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, delete_like_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.success = iprot.readBool();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, delete_like_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.isSetSuccess()) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeBool(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class delete_like_resultTupleSchemeFactory implements SchemeFactory {
			public delete_like_resultTupleScheme getScheme() {
				return new delete_like_resultTupleScheme();
			}
		}

		private static class delete_like_resultTupleScheme extends TupleScheme<delete_like_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, delete_like_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeBool(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, delete_like_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readBool();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class add_post_args implements org.apache.thrift.TBase<add_post_args, add_post_args._Fields>, java.io.Serializable, Cloneable, Comparable<add_post_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("add_post_args");

		private static final org.apache.thrift.protocol.TField TOKEN_FIELD_DESC = new org.apache.thrift.protocol.TField("token", org.apache.thrift.protocol.TType.STRING, (short) 1);
		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 2);
		private static final org.apache.thrift.protocol.TField WHO_FIELD_DESC = new org.apache.thrift.protocol.TField("who", org.apache.thrift.protocol.TType.STRING, (short) 3);
		private static final org.apache.thrift.protocol.TField CONTENT_FIELD_DESC = new org.apache.thrift.protocol.TField("content", org.apache.thrift.protocol.TType.STRING, (short) 4);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new add_post_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new add_post_argsTupleSchemeFactory());
		}

		public String token; // required
		public String tripPath; // required
		public String who; // required
		public String content; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TOKEN((short) 1, "token"), TRIP_PATH((short) 2, "tripPath"), WHO((short) 3, "who"), CONTENT((short) 4, "content");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TOKEN
					return TOKEN;
				case 2: // TRIP_PATH
					return TRIP_PATH;
				case 3: // WHO
					return WHO;
				case 4: // CONTENT
					return CONTENT;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TOKEN, new org.apache.thrift.meta_data.FieldMetaData("token", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.WHO, new org.apache.thrift.meta_data.FieldMetaData("who", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			tmpMap.put(_Fields.CONTENT, new org.apache.thrift.meta_data.FieldMetaData("content", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(add_post_args.class, metaDataMap);
		}

		public add_post_args() {
		}

		public add_post_args(String token, String tripPath, String who, String content) {
			this();
			this.token = token;
			this.tripPath = tripPath;
			this.who = who;
			this.content = content;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public add_post_args(add_post_args other) {
			if (other.isSetToken()) {
				this.token = other.token;
			}
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
			if (other.isSetWho()) {
				this.who = other.who;
			}
			if (other.isSetContent()) {
				this.content = other.content;
			}
		}

		public add_post_args deepCopy() {
			return new add_post_args(this);
		}

		@Override
		public void clear() {
			this.token = null;
			this.tripPath = null;
			this.who = null;
			this.content = null;
		}

		public String getToken() {
			return this.token;
		}

		public add_post_args setToken(String token) {
			this.token = token;
			return this;
		}

		public void unsetToken() {
			this.token = null;
		}

		/**
		 * Returns true if field token is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetToken() {
			return this.token != null;
		}

		public void setTokenIsSet(boolean value) {
			if (!value) {
				this.token = null;
			}
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public add_post_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public String getWho() {
			return this.who;
		}

		public add_post_args setWho(String who) {
			this.who = who;
			return this;
		}

		public void unsetWho() {
			this.who = null;
		}

		/**
		 * Returns true if field who is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetWho() {
			return this.who != null;
		}

		public void setWhoIsSet(boolean value) {
			if (!value) {
				this.who = null;
			}
		}

		public String getContent() {
			return this.content;
		}

		public add_post_args setContent(String content) {
			this.content = content;
			return this;
		}

		public void unsetContent() {
			this.content = null;
		}

		/**
		 * Returns true if field content is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetContent() {
			return this.content != null;
		}

		public void setContentIsSet(boolean value) {
			if (!value) {
				this.content = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TOKEN:
				if (value == null) {
					unsetToken();
				} else {
					setToken((String) value);
				}
				break;

			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			case WHO:
				if (value == null) {
					unsetWho();
				} else {
					setWho((String) value);
				}
				break;

			case CONTENT:
				if (value == null) {
					unsetContent();
				} else {
					setContent((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TOKEN:
				return getToken();

			case TRIP_PATH:
				return getTripPath();

			case WHO:
				return getWho();

			case CONTENT:
				return getContent();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TOKEN:
				return isSetToken();
			case TRIP_PATH:
				return isSetTripPath();
			case WHO:
				return isSetWho();
			case CONTENT:
				return isSetContent();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof add_post_args)
				return this.equals((add_post_args) that);
			return false;
		}

		public boolean equals(add_post_args that) {
			if (that == null)
				return false;

			boolean this_present_token = true && this.isSetToken();
			boolean that_present_token = true && that.isSetToken();
			if (this_present_token || that_present_token) {
				if (!(this_present_token && that_present_token))
					return false;
				if (!this.token.equals(that.token))
					return false;
			}

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			boolean this_present_who = true && this.isSetWho();
			boolean that_present_who = true && that.isSetWho();
			if (this_present_who || that_present_who) {
				if (!(this_present_who && that_present_who))
					return false;
				if (!this.who.equals(that.who))
					return false;
			}

			boolean this_present_content = true && this.isSetContent();
			boolean that_present_content = true && that.isSetContent();
			if (this_present_content || that_present_content) {
				if (!(this_present_content && that_present_content))
					return false;
				if (!this.content.equals(that.content))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(add_post_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetToken()).compareTo(other.isSetToken());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetToken()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.token, other.token);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetWho()).compareTo(other.isSetWho());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetWho()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.who, other.who);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			lastComparison = Boolean.valueOf(isSetContent()).compareTo(other.isSetContent());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetContent()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.content, other.content);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("add_post_args(");
			boolean first = true;

			sb.append("token:");
			if (this.token == null) {
				sb.append("null");
			} else {
				sb.append(this.token);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("who:");
			if (this.who == null) {
				sb.append("null");
			} else {
				sb.append(this.who);
			}
			first = false;
			if (!first)
				sb.append(", ");
			sb.append("content:");
			if (this.content == null) {
				sb.append("null");
			} else {
				sb.append(this.content);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class add_post_argsStandardSchemeFactory implements SchemeFactory {
			public add_post_argsStandardScheme getScheme() {
				return new add_post_argsStandardScheme();
			}
		}

		private static class add_post_argsStandardScheme extends StandardScheme<add_post_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, add_post_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TOKEN
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.token = iprot.readString();
							struct.setTokenIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 2: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 3: // WHO
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.who = iprot.readString();
							struct.setWhoIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					case 4: // CONTENT
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.content = iprot.readString();
							struct.setContentIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, add_post_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.token != null) {
					oprot.writeFieldBegin(TOKEN_FIELD_DESC);
					oprot.writeString(struct.token);
					oprot.writeFieldEnd();
				}
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				if (struct.who != null) {
					oprot.writeFieldBegin(WHO_FIELD_DESC);
					oprot.writeString(struct.who);
					oprot.writeFieldEnd();
				}
				if (struct.content != null) {
					oprot.writeFieldBegin(CONTENT_FIELD_DESC);
					oprot.writeString(struct.content);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class add_post_argsTupleSchemeFactory implements SchemeFactory {
			public add_post_argsTupleScheme getScheme() {
				return new add_post_argsTupleScheme();
			}
		}

		private static class add_post_argsTupleScheme extends TupleScheme<add_post_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, add_post_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetToken()) {
					optionals.set(0);
				}
				if (struct.isSetTripPath()) {
					optionals.set(1);
				}
				if (struct.isSetWho()) {
					optionals.set(2);
				}
				if (struct.isSetContent()) {
					optionals.set(3);
				}
				oprot.writeBitSet(optionals, 4);
				if (struct.isSetToken()) {
					oprot.writeString(struct.token);
				}
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
				if (struct.isSetWho()) {
					oprot.writeString(struct.who);
				}
				if (struct.isSetContent()) {
					oprot.writeString(struct.content);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, add_post_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(4);
				if (incoming.get(0)) {
					struct.token = iprot.readString();
					struct.setTokenIsSet(true);
				}
				if (incoming.get(1)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
				if (incoming.get(2)) {
					struct.who = iprot.readString();
					struct.setWhoIsSet(true);
				}
				if (incoming.get(3)) {
					struct.content = iprot.readString();
					struct.setContentIsSet(true);
				}
			}
		}

	}

	public static class add_post_result implements org.apache.thrift.TBase<add_post_result, add_post_result._Fields>, java.io.Serializable, Cloneable, Comparable<add_post_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("add_post_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.BOOL, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new add_post_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new add_post_resultTupleSchemeFactory());
		}

		public boolean success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __SUCCESS_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(add_post_result.class, metaDataMap);
		}

		public add_post_result() {
		}

		public add_post_result(boolean success) {
			this();
			this.success = success;
			setSuccessIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public add_post_result(add_post_result other) {
			__isset_bitfield = other.__isset_bitfield;
			this.success = other.success;
		}

		public add_post_result deepCopy() {
			return new add_post_result(this);
		}

		@Override
		public void clear() {
			setSuccessIsSet(false);
			this.success = false;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public add_post_result setSuccess(boolean success) {
			this.success = success;
			setSuccessIsSet(true);
			return this;
		}

		public void unsetSuccess() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return EncodingUtils.testBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		public void setSuccessIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SUCCESS_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Boolean) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return Boolean.valueOf(isSuccess());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof add_post_result)
				return this.equals((add_post_result) that);
			return false;
		}

		public boolean equals(add_post_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true;
			boolean that_present_success = true;
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (this.success != that.success)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(add_post_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("add_post_result(");
			boolean first = true;

			sb.append("success:");
			sb.append(this.success);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class add_post_resultStandardSchemeFactory implements SchemeFactory {
			public add_post_resultStandardScheme getScheme() {
				return new add_post_resultStandardScheme();
			}
		}

		private static class add_post_resultStandardScheme extends StandardScheme<add_post_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, add_post_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.success = iprot.readBool();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, add_post_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.isSetSuccess()) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeBool(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class add_post_resultTupleSchemeFactory implements SchemeFactory {
			public add_post_resultTupleScheme getScheme() {
				return new add_post_resultTupleScheme();
			}
		}

		private static class add_post_resultTupleScheme extends TupleScheme<add_post_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, add_post_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeBool(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, add_post_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readBool();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class add_view_args implements org.apache.thrift.TBase<add_view_args, add_view_args._Fields>, java.io.Serializable, Cloneable, Comparable<add_view_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("add_view_args");

		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 1);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new add_view_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new add_view_argsTupleSchemeFactory());
		}

		public String tripPath; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TRIP_PATH((short) 1, "tripPath");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TRIP_PATH
					return TRIP_PATH;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(add_view_args.class, metaDataMap);
		}

		public add_view_args() {
		}

		public add_view_args(String tripPath) {
			this();
			this.tripPath = tripPath;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public add_view_args(add_view_args other) {
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
		}

		public add_view_args deepCopy() {
			return new add_view_args(this);
		}

		@Override
		public void clear() {
			this.tripPath = null;
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public add_view_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TRIP_PATH:
				return getTripPath();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TRIP_PATH:
				return isSetTripPath();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof add_view_args)
				return this.equals((add_view_args) that);
			return false;
		}

		public boolean equals(add_view_args that) {
			if (that == null)
				return false;

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(add_view_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("add_view_args(");
			boolean first = true;

			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class add_view_argsStandardSchemeFactory implements SchemeFactory {
			public add_view_argsStandardScheme getScheme() {
				return new add_view_argsStandardScheme();
			}
		}

		private static class add_view_argsStandardScheme extends StandardScheme<add_view_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, add_view_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, add_view_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class add_view_argsTupleSchemeFactory implements SchemeFactory {
			public add_view_argsTupleScheme getScheme() {
				return new add_view_argsTupleScheme();
			}
		}

		private static class add_view_argsTupleScheme extends TupleScheme<add_view_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, add_view_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetTripPath()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, add_view_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
			}
		}

	}

	public static class add_view_result implements org.apache.thrift.TBase<add_view_result, add_view_result._Fields>, java.io.Serializable, Cloneable, Comparable<add_view_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("add_view_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.BOOL, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new add_view_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new add_view_resultTupleSchemeFactory());
		}

		public boolean success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __SUCCESS_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(add_view_result.class, metaDataMap);
		}

		public add_view_result() {
		}

		public add_view_result(boolean success) {
			this();
			this.success = success;
			setSuccessIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public add_view_result(add_view_result other) {
			__isset_bitfield = other.__isset_bitfield;
			this.success = other.success;
		}

		public add_view_result deepCopy() {
			return new add_view_result(this);
		}

		@Override
		public void clear() {
			setSuccessIsSet(false);
			this.success = false;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public add_view_result setSuccess(boolean success) {
			this.success = success;
			setSuccessIsSet(true);
			return this;
		}

		public void unsetSuccess() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return EncodingUtils.testBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		public void setSuccessIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SUCCESS_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Boolean) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return Boolean.valueOf(isSuccess());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof add_view_result)
				return this.equals((add_view_result) that);
			return false;
		}

		public boolean equals(add_view_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true;
			boolean that_present_success = true;
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (this.success != that.success)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(add_view_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("add_view_result(");
			boolean first = true;

			sb.append("success:");
			sb.append(this.success);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class add_view_resultStandardSchemeFactory implements SchemeFactory {
			public add_view_resultStandardScheme getScheme() {
				return new add_view_resultStandardScheme();
			}
		}

		private static class add_view_resultStandardScheme extends StandardScheme<add_view_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, add_view_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.success = iprot.readBool();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, add_view_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.isSetSuccess()) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeBool(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class add_view_resultTupleSchemeFactory implements SchemeFactory {
			public add_view_resultTupleScheme getScheme() {
				return new add_view_resultTupleScheme();
			}
		}

		private static class add_view_resultTupleScheme extends TupleScheme<add_view_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, add_view_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeBool(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, add_view_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readBool();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

	public static class check_trip_public_args implements org.apache.thrift.TBase<check_trip_public_args, check_trip_public_args._Fields>, java.io.Serializable, Cloneable, Comparable<check_trip_public_args> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("check_trip_public_args");

		private static final org.apache.thrift.protocol.TField TRIP_PATH_FIELD_DESC = new org.apache.thrift.protocol.TField("tripPath", org.apache.thrift.protocol.TType.STRING, (short) 1);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new check_trip_public_argsStandardSchemeFactory());
			schemes.put(TupleScheme.class, new check_trip_public_argsTupleSchemeFactory());
		}

		public String tripPath; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			TRIP_PATH((short) 1, "tripPath");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 1: // TRIP_PATH
					return TRIP_PATH;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.TRIP_PATH, new org.apache.thrift.meta_data.FieldMetaData("tripPath", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(check_trip_public_args.class, metaDataMap);
		}

		public check_trip_public_args() {
		}

		public check_trip_public_args(String tripPath) {
			this();
			this.tripPath = tripPath;
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public check_trip_public_args(check_trip_public_args other) {
			if (other.isSetTripPath()) {
				this.tripPath = other.tripPath;
			}
		}

		public check_trip_public_args deepCopy() {
			return new check_trip_public_args(this);
		}

		@Override
		public void clear() {
			this.tripPath = null;
		}

		public String getTripPath() {
			return this.tripPath;
		}

		public check_trip_public_args setTripPath(String tripPath) {
			this.tripPath = tripPath;
			return this;
		}

		public void unsetTripPath() {
			this.tripPath = null;
		}

		/**
		 * Returns true if field tripPath is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetTripPath() {
			return this.tripPath != null;
		}

		public void setTripPathIsSet(boolean value) {
			if (!value) {
				this.tripPath = null;
			}
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case TRIP_PATH:
				if (value == null) {
					unsetTripPath();
				} else {
					setTripPath((String) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case TRIP_PATH:
				return getTripPath();

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case TRIP_PATH:
				return isSetTripPath();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof check_trip_public_args)
				return this.equals((check_trip_public_args) that);
			return false;
		}

		public boolean equals(check_trip_public_args that) {
			if (that == null)
				return false;

			boolean this_present_tripPath = true && this.isSetTripPath();
			boolean that_present_tripPath = true && that.isSetTripPath();
			if (this_present_tripPath || that_present_tripPath) {
				if (!(this_present_tripPath && that_present_tripPath))
					return false;
				if (!this.tripPath.equals(that.tripPath))
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(check_trip_public_args other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetTripPath()).compareTo(other.isSetTripPath());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetTripPath()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tripPath, other.tripPath);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("check_trip_public_args(");
			boolean first = true;

			sb.append("tripPath:");
			if (this.tripPath == null) {
				sb.append("null");
			} else {
				sb.append(this.tripPath);
			}
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class check_trip_public_argsStandardSchemeFactory implements SchemeFactory {
			public check_trip_public_argsStandardScheme getScheme() {
				return new check_trip_public_argsStandardScheme();
			}
		}

		private static class check_trip_public_argsStandardScheme extends StandardScheme<check_trip_public_args> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, check_trip_public_args struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 1: // TRIP_PATH
						if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
							struct.tripPath = iprot.readString();
							struct.setTripPathIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, check_trip_public_args struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.tripPath != null) {
					oprot.writeFieldBegin(TRIP_PATH_FIELD_DESC);
					oprot.writeString(struct.tripPath);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class check_trip_public_argsTupleSchemeFactory implements SchemeFactory {
			public check_trip_public_argsTupleScheme getScheme() {
				return new check_trip_public_argsTupleScheme();
			}
		}

		private static class check_trip_public_argsTupleScheme extends TupleScheme<check_trip_public_args> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, check_trip_public_args struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetTripPath()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetTripPath()) {
					oprot.writeString(struct.tripPath);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, check_trip_public_args struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.tripPath = iprot.readString();
					struct.setTripPathIsSet(true);
				}
			}
		}

	}

	public static class check_trip_public_result implements org.apache.thrift.TBase<check_trip_public_result, check_trip_public_result._Fields>, java.io.Serializable, Cloneable, Comparable<check_trip_public_result> {
		private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("check_trip_public_result");

		private static final org.apache.thrift.protocol.TField SUCCESS_FIELD_DESC = new org.apache.thrift.protocol.TField("success", org.apache.thrift.protocol.TType.BOOL, (short) 0);

		private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
		static {
			schemes.put(StandardScheme.class, new check_trip_public_resultStandardSchemeFactory());
			schemes.put(TupleScheme.class, new check_trip_public_resultTupleSchemeFactory());
		}

		public boolean success; // required

		/**
		 * The set of fields this struct contains, along with convenience
		 * methods for finding and manipulating them.
		 */
		public enum _Fields implements org.apache.thrift.TFieldIdEnum {
			SUCCESS((short) 0, "success");

			private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

			static {
				for (_Fields field : EnumSet.allOf(_Fields.class)) {
					byName.put(field.getFieldName(), field);
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, or null if its
			 * not found.
			 */
			public static _Fields findByThriftId(int fieldId) {
				switch (fieldId) {
				case 0: // SUCCESS
					return SUCCESS;
				default:
					return null;
				}
			}

			/**
			 * Find the _Fields constant that matches fieldId, throwing an
			 * exception if it is not found.
			 */
			public static _Fields findByThriftIdOrThrow(int fieldId) {
				_Fields fields = findByThriftId(fieldId);
				if (fields == null)
					throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
				return fields;
			}

			/**
			 * Find the _Fields constant that matches name, or null if its not
			 * found.
			 */
			public static _Fields findByName(String name) {
				return byName.get(name);
			}

			private final short _thriftId;
			private final String _fieldName;

			_Fields(short thriftId, String fieldName) {
				_thriftId = thriftId;
				_fieldName = fieldName;
			}

			public short getThriftFieldId() {
				return _thriftId;
			}

			public String getFieldName() {
				return _fieldName;
			}
		}

		// isset id assignments
		private static final int __SUCCESS_ISSET_ID = 0;
		private byte __isset_bitfield = 0;
		public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
		static {
			Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
			tmpMap.put(_Fields.SUCCESS, new org.apache.thrift.meta_data.FieldMetaData("success", org.apache.thrift.TFieldRequirementType.DEFAULT, new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
			metaDataMap = Collections.unmodifiableMap(tmpMap);
			org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(check_trip_public_result.class, metaDataMap);
		}

		public check_trip_public_result() {
		}

		public check_trip_public_result(boolean success) {
			this();
			this.success = success;
			setSuccessIsSet(true);
		}

		/**
		 * Performs a deep copy on <i>other</i>.
		 */
		public check_trip_public_result(check_trip_public_result other) {
			__isset_bitfield = other.__isset_bitfield;
			this.success = other.success;
		}

		public check_trip_public_result deepCopy() {
			return new check_trip_public_result(this);
		}

		@Override
		public void clear() {
			setSuccessIsSet(false);
			this.success = false;
		}

		public boolean isSuccess() {
			return this.success;
		}

		public check_trip_public_result setSuccess(boolean success) {
			this.success = success;
			setSuccessIsSet(true);
			return this;
		}

		public void unsetSuccess() {
			__isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		/**
		 * Returns true if field success is set (has been assigned a value) and
		 * false otherwise
		 */
		public boolean isSetSuccess() {
			return EncodingUtils.testBit(__isset_bitfield, __SUCCESS_ISSET_ID);
		}

		public void setSuccessIsSet(boolean value) {
			__isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SUCCESS_ISSET_ID, value);
		}

		public void setFieldValue(_Fields field, Object value) {
			switch (field) {
			case SUCCESS:
				if (value == null) {
					unsetSuccess();
				} else {
					setSuccess((Boolean) value);
				}
				break;

			}
		}

		public Object getFieldValue(_Fields field) {
			switch (field) {
			case SUCCESS:
				return Boolean.valueOf(isSuccess());

			}
			throw new IllegalStateException();
		}

		/**
		 * Returns true if field corresponding to fieldID is set (has been
		 * assigned a value) and false otherwise
		 */
		public boolean isSet(_Fields field) {
			if (field == null) {
				throw new IllegalArgumentException();
			}

			switch (field) {
			case SUCCESS:
				return isSetSuccess();
			}
			throw new IllegalStateException();
		}

		@Override
		public boolean equals(Object that) {
			if (that == null)
				return false;
			if (that instanceof check_trip_public_result)
				return this.equals((check_trip_public_result) that);
			return false;
		}

		public boolean equals(check_trip_public_result that) {
			if (that == null)
				return false;

			boolean this_present_success = true;
			boolean that_present_success = true;
			if (this_present_success || that_present_success) {
				if (!(this_present_success && that_present_success))
					return false;
				if (this.success != that.success)
					return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public int compareTo(check_trip_public_result other) {
			if (!getClass().equals(other.getClass())) {
				return getClass().getName().compareTo(other.getClass().getName());
			}

			int lastComparison = 0;

			lastComparison = Boolean.valueOf(isSetSuccess()).compareTo(other.isSetSuccess());
			if (lastComparison != 0) {
				return lastComparison;
			}
			if (isSetSuccess()) {
				lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.success, other.success);
				if (lastComparison != 0) {
					return lastComparison;
				}
			}
			return 0;
		}

		public _Fields fieldForId(int fieldId) {
			return _Fields.findByThriftId(fieldId);
		}

		public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
			schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
		}

		public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
			schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder("check_trip_public_result(");
			boolean first = true;

			sb.append("success:");
			sb.append(this.success);
			first = false;
			sb.append(")");
			return sb.toString();
		}

		public void validate() throws org.apache.thrift.TException {
			// check for required fields
			// check for sub-struct validity
		}

		private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
			try {
				write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
			try {
				// it doesn't seem like you should have to do this, but java
				// serialization is wacky, and doesn't call the default
				// constructor.
				__isset_bitfield = 0;
				read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
			} catch (org.apache.thrift.TException te) {
				throw new java.io.IOException(te);
			}
		}

		private static class check_trip_public_resultStandardSchemeFactory implements SchemeFactory {
			public check_trip_public_resultStandardScheme getScheme() {
				return new check_trip_public_resultStandardScheme();
			}
		}

		private static class check_trip_public_resultStandardScheme extends StandardScheme<check_trip_public_result> {

			public void read(org.apache.thrift.protocol.TProtocol iprot, check_trip_public_result struct) throws org.apache.thrift.TException {
				org.apache.thrift.protocol.TField schemeField;
				iprot.readStructBegin();
				while (true) {
					schemeField = iprot.readFieldBegin();
					if (schemeField.type == org.apache.thrift.protocol.TType.STOP) {
						break;
					}
					switch (schemeField.id) {
					case 0: // SUCCESS
						if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
							struct.success = iprot.readBool();
							struct.setSuccessIsSet(true);
						} else {
							org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
						}
						break;
					default:
						org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
					}
					iprot.readFieldEnd();
				}
				iprot.readStructEnd();

				// check for required fields of primitive type, which can't be
				// checked in the validate method
				struct.validate();
			}

			public void write(org.apache.thrift.protocol.TProtocol oprot, check_trip_public_result struct) throws org.apache.thrift.TException {
				struct.validate();

				oprot.writeStructBegin(STRUCT_DESC);
				if (struct.isSetSuccess()) {
					oprot.writeFieldBegin(SUCCESS_FIELD_DESC);
					oprot.writeBool(struct.success);
					oprot.writeFieldEnd();
				}
				oprot.writeFieldStop();
				oprot.writeStructEnd();
			}

		}

		private static class check_trip_public_resultTupleSchemeFactory implements SchemeFactory {
			public check_trip_public_resultTupleScheme getScheme() {
				return new check_trip_public_resultTupleScheme();
			}
		}

		private static class check_trip_public_resultTupleScheme extends TupleScheme<check_trip_public_result> {

			@Override
			public void write(org.apache.thrift.protocol.TProtocol prot, check_trip_public_result struct) throws org.apache.thrift.TException {
				TTupleProtocol oprot = (TTupleProtocol) prot;
				BitSet optionals = new BitSet();
				if (struct.isSetSuccess()) {
					optionals.set(0);
				}
				oprot.writeBitSet(optionals, 1);
				if (struct.isSetSuccess()) {
					oprot.writeBool(struct.success);
				}
			}

			@Override
			public void read(org.apache.thrift.protocol.TProtocol prot, check_trip_public_result struct) throws org.apache.thrift.TException {
				TTupleProtocol iprot = (TTupleProtocol) prot;
				BitSet incoming = iprot.readBitSet(1);
				if (incoming.get(0)) {
					struct.success = iprot.readBool();
					struct.setSuccessIsSet(true);
				}
			}
		}

	}

}
