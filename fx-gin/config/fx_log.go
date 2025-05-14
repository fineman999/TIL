// config/fx_logger.go
package config

import (
	"github.com/sirupsen/logrus"
	"go.uber.org/fx/fxevent"
)

type LogrusFxLogger struct {
	logger *logrus.Logger
}

func NewFxLogger(logger *logrus.Logger) fxevent.Logger {
	logger.SetFormatter(&logrus.TextFormatter{
		DisableTimestamp: true, // 타임스탬프 비활성화
	})
	return &LogrusFxLogger{logger: logger}
}

func (l *LogrusFxLogger) LogEvent(event fxevent.Event) {
	switch e := event.(type) {
	case *fxevent.OnStartExecuting:
		l.logger.Debugf("[Fx] HOOK OnStart\t%s executing (caller: %s)", e.FunctionName, e.CallerName)
	case *fxevent.OnStartExecuted:
		if e.Err != nil {
			l.logger.Errorf("[Fx] HOOK OnStart\t%s called by %s failed: %v", e.FunctionName, e.CallerName, e.Err)
		} else {
			l.logger.Debugf("[Fx] HOOK OnStart\t%s called by %s ran successfully in %v", e.FunctionName, e.CallerName, e.Runtime)
		}
	case *fxevent.OnStopExecuting:
		l.logger.Debugf("[Fx] HOOK OnStop\t%s executing (caller: %s)", e.FunctionName, e.CallerName)
	case *fxevent.OnStopExecuted:
		if e.Err != nil {
			l.logger.Errorf("[Fx] HOOK OnStop\t%s called by %s failed: %v", e.FunctionName, e.CallerName, e.Err)
		} else {
			l.logger.Debugf("[Fx] HOOK OnStop\t%s called by %s ran successfully in %v", e.FunctionName, e.CallerName, e.Runtime)
		}
	case *fxevent.Supplied:
		l.logger.Debugf("[Fx] SUPPLY\t%s", e.TypeName)
	case *fxevent.Provided:
		l.logger.Debugf("[Fx] PROVIDE\t%s <= %s", e.OutputTypeNames[0], e.ConstructorName)
	case *fxevent.Invoking:
		l.logger.Debugf("[Fx] INVOKE\t%s", e.FunctionName)
	case *fxevent.Invoked:
		if e.Err != nil {
			l.logger.Errorf("[Fx] INVOKE\t%s failed: %v", e.FunctionName, e.Err)
		}
	case *fxevent.Stopping:
		l.logger.Debugf("[Fx] STOPPING")
	case *fxevent.Stopped:
		l.logger.Debugf("[Fx] STOPPED")
	case *fxevent.RollingBack:
		l.logger.Errorf("[Fx] ROLLING BACK due to start error: %v", e.StartErr)
	case *fxevent.RolledBack:
		if e.Err != nil {
			l.logger.Errorf("[Fx] ROLLBACK failed: %v", e.Err)
		}
	case *fxevent.Started:
		l.logger.Debugf("[Fx] RUNNING")
	case *fxevent.LoggerInitialized:
		if e.Err != nil {
			l.logger.Errorf("[Fx] LOGGER initialization failed: %v", e.Err)
		} else {
			l.logger.Debugf("[Fx] LOGGER initialized: %s", e.ConstructorName)
		}
	}
}
