package igorekpotworek.infrastructure.server.lifecycle;

import igorekpotworek.infrastructure.server.Session;

import java.io.PrintWriter;
import java.util.function.BiConsumer;

public interface LifecycleHook extends BiConsumer<PrintWriter, Session> {

  interface StartupHook extends LifecycleHook {}

  interface ShutdownHook extends LifecycleHook {}
}
