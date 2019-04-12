package com.rds.platform.mina2;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.rds.platform.mina2.coder.HCoderFactory;
import com.rds.platform.utils.CacheUtils;

public class MinaClient {
	private static final int bindPort=6008;
	private NioSocketConnector connector;
    private ConnectFuture future;
    private IoSession session;
    
    private Map<String, String> cache = CacheUtils.getInstace().getCache();
 
    public boolean connect() {
 
        // 创建�?个socket连接
        connector = new NioSocketConnector();
        // 设置链接超时时间
//        connector.setConnectTimeoutMillis(3000);
        // 获取过滤器链
        DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
        // 添加编码过滤�? 处理乱码、编码问�?
        ProtocolCodecFilter filter= new ProtocolCodecFilter(new HCoderFactory(Charset.forName("UTF-8"))); 
        filterChain.addLast("objectFilter", filter);
 
        // 消息核心处理�?
        connector.setHandler(new MinaClientHanlder());
        // 设置链接超时时间     
        connector.setConnectTimeoutCheckInterval(30);
 
        // 连接服务器，知道端口、地�?
        future = connector.connect(new InetSocketAddress(cache.get("fcs-host"),Integer.parseInt(cache.get("fcs-port"))));
        // 等待连接创建完成
        future.awaitUninterruptibly();
        
        // 获取当前session
        session = future.getSession();
        return true;
    }
 
    public void setAttribute(Object key, Object value) {
        session.setAttribute(key, value);
    }
 
    public void send(String message) {
        session.write(message);
    }
 
    @SuppressWarnings("deprecation")
	public boolean close() {
//        CloseFuture future = session.getCloseFuture();
//        future.awaitUninterruptibly(1000);
//        connector.dispose();
    	session.close();
        return true;
    }
 
    public SocketConnector getConnector() {
        return connector;
    }
 
    public IoSession getSession() {
        return session;
    }

}
