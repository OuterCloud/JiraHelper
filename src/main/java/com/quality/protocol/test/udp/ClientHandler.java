package com.quality.protocol.test.udp;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.quality.protocol.message.RequestMessage;
import com.quality.protocol.message.ResponseMessage;

class ClientHandler extends IoHandlerAdapter {
    private final String values;
     
    public ClientHandler(String values){
        this.values = values;
    }
 
    @Override
    public void sessionCreated(IoSession session) throws Exception {
        System.out.println("sessionCreated is invoked....");
    }
 
    /**
     * 发送消息
     * @see =================================================================================================
     * @see 客户端连接有两个事件：sessionCreated和sessionOpened
     * @see sessionCreated是由IoProcessor线程触发的，sessionOpened跟在其后，是由业务线程触发的
     * @see 由于Mina中的IoProcessor线程非常少，因此sessionCreated通常用于处理耗时短的操作
     * @see 而将业务初始化等功能放在sessionOpened事件中，比如发送消息
     * @see =================================================================================================
     * @see 我们可以在sessionOpened()、messageReceived()中使用IoSession.write()方法发送消息
     * @see 因为在这两个方法中，TCP连接都是打开的状态，只不过发送的时机不同
     * @see sessionOpened()是在TCP连接建立之后，接收到数据之前发送
     * @see messageReceived()是在接收到数据之后发送
     * @see =================================================================================================
     */
    @Override
    public void sessionOpened(IoSession session) throws Exception {
    	RequestMessage req = new RequestMessage();
    	req.setRequest(values);
        session.write(req);
        for(int i = 0; i < 100; i++){
        	req.setRequest("UDP-hello-" + i);
            session.write(req); 
            try{
            	Thread.sleep(100);
            }catch(InterruptedException e){}
        }
    }
 
    public static int i = 0;
    @Override
    public void messageReceived(IoSession session, Object message){
    	if(message instanceof ResponseMessage){
    		ResponseMessage res = (ResponseMessage)message;
	    	System.out.println("UDP client接受到的信息为：" + res.getResponse() + "-" + i);
	    	i++;
    	}
    }
    /**
     * 关于TCP连接的关闭
     * @see 无论在客户端还是服务端，IoSession都用于表示底层的一个TCP连接
     * @see 那么你会发现无论是Server端还是Client端的IoSession调用close()后，TCP连接虽然显示关闭，但主线程仍在运行，即JVM并未退出
     * @see 这是因为IoSession的close()仅仅是关闭了TCP的连接通道，并没有关闭Server端和Client端的程序
     * @see 此时需要调用IoService.dispose()停止Server端和Client端
     */
    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        System.out.println("与" + session.getRemoteAddress() + "通信过程中出现错误:[" + cause.getMessage() + "]..连接即将关闭....");
        cause.printStackTrace();
        //关闭IoSession，该操作也是异步的....true表示立即关闭，false表示所有写操作都flush后关闭
        session.close(false);
        //IoSession.IoService getService()用于返回与当前会话对象关联的IoService实例
        session.getService().dispose();
    }
}