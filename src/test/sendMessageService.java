package test;
import jadex.commons.future.IFuture;


public interface sendMessageService
{
	IFuture<String> sendMessage ();
}