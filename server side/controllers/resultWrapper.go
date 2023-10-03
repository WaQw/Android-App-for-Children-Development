package controllers

type ResultWrapper struct {
	Code int         `json:"code"`
	Msg  string      `json:"msg"`
	Data interface{} `json:"data"`
}

func GenerateResultWrapper() *ResultWrapper {
	return &ResultWrapper{
		Code: FailCode,
		Msg:  "",
		Data: nil,
	}
}

func GenerateErrResultWrapper(code int, msg string) *ResultWrapper {
	return &ResultWrapper{
		Code: code,
		Msg:  msg,
		Data: nil,
	}
}
