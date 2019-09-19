package com.zongyi.flavoradapter;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.zongyi.channeladapter.ChannelAdapterHuawei;
import com.zongyi.channeladapter.ChannelAdapterMain;

public class FlavorAdapterHuawei extends FlavorAdapter{

    private ChannelAdapterHuawei _channelAdapterHuawei;
    private Activity _activity;

    @Override
    public void onCreate(Activity activity) {
        _activity = activity;
        _channelAdapterHuawei = new ChannelAdapterHuawei();
        _channelAdapterHuawei.UNCHECK_PAYREQUESTID_FILE = "pay_request_ids";
        _channelAdapterHuawei.game_priv_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCbj7BUNZpNyM3+H0t3f3KHSH+w8EMmWBy8SeYzH6/mvJ1c4I8ADa3Cw2WdozS4yKn0JrmwIUhNL5ms6Mp8dFjxCZvskC+/nbgUcn4whNqaUPCg17bd4pkm3vn+nQyf/Fpb4SLpNgFyyUDvj/J/m9b4KET9hFo2Cmyjp4DZNwUsc46cXaenhhH6FoAql/cov8bVwZCmIC0G+VZs0coiOdsf3Q893lQ0kcVATmUPhN4e8FkSqVjP9AuoEwNxtRCaDfGNhUstPdfasQzCoaTGzwyhOcRSzd71nB7tQnwk0pFmWsK95Y5d2UOkEQWk38JybHIH2A/7/w3uGO0QAW47CfxDAgMBAAECggEAH2Qyl/utJK816mcUSov8Z5JYujLxygYT3mcnPf/R9kTvmHN0R/V6sAfQNKxfkj5kFXHPHNzKg1DsuPjNC6pK3HEs9uLtz8QbBn1IO7BDkMpulHaTX1k/wJlUZ233wz0gWtQmwFr2ldxI707kB55CFhfCPEp+DdOysp+1n5+7uolSeTnSDabesizivmkRdkGBZ64APc9MVTI5pfG1SuDXkvqdgoRmNBmEzewtCG7S6IEqIpOyEOS2Bs+DdDiRoLvsxF4Acc3SXxFieqwwyA6jgiyfEs2oxyssgfSzVx42fKz2jZSUkqsGWtMxYwCPYS2adSLt+IjnYOljT3b4GAFYEQKBgQDT7MlN8EFOKg3RXMryq1Vp8+X6fJ4NQjMrzOFscpnSFntpk76EBzuyd3y+CQztH6feRjG8wOdYJ4Rm4NA4arNs1a9do7lWr1OdUir53HHVqxJrby/Sxfmas/NYzO5JZNXlFPKh5X2SsIZZ4zhLs8qVpPMSz2e/ldam+R5IEUG8iwKBgQC76gNvvD6zB2xV7c1UaOqwU/ETHfX+du178QAUWWyw6Atmp290ViWQ3oTTAzgiGapqRchWHArr/vgQnjYXA0klLy2A/D43A4hNFDuFubEZIrYuBh4B3znC3IuG67F06r8CvOxM8SfGSTTWLxABTyJ6SeWRTnMIPHxXYNiWqw+eKQKBgQCXCptKziJtSDHC9uS6YfylYCyYA3tOLdF0tTgw59jT0CwzShat2M3mW5zvgFnTIw2ssNDAG6iGvttxy2b73qh0uDOdhJQSwTDR3n88x63mQGAHbB5kla9ZYEnfD2gBrTl5ZR2s33hJNUD8DaLQ6ZpIX2DTFjl2ZZ+wL7syWAJHEwKBgDdIzMQFvOiM6YmKUdtglpNTztwR93pq4MmTCLPz7y9IxXnpDrhKU/kADlHKY6xcoq9rYlo7G9d8htzUvxMcK1veOmP1UhR7XJFYv35DOGAtvy1hLVjdpK+4lPAXzXiZS6LxlA+67zm99eaBqBLKhlk1/m02iE9HBEnsuyvwncKpAoGBAKid2XnaOo02SQzgc/e+2HMRXJD3M3pFVtXowvglGaVAxSGORt+cvWZs2vYXdpJdEg4VuERH6VxtMxHQHKpjVElXmRr9fMHUlBpIEEUWQXKpbn3HHh1whGd7hw3qfaLsmfNTYqWALIJi9m/LKVM6RRVlOh/9/Q3HSIdh589tMBYl";
        _channelAdapterHuawei.game_public_key ="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm4+wVDWaTcjN/h9Ld39yh0h/sPBDJlgcvEnmMx+v5rydXOCPAA2twsNlnaM0uMip9Ca5sCFITS+ZrOjKfHRY8Qmb7JAvv524FHJ+MITamlDwoNe23eKZJt75/p0Mn/xaW+Ei6TYBcslA74/yf5vW+ChE/YRaNgpso6eA2TcFLHOOnF2np4YR+haAKpf3KL/G1cGQpiAtBvlWbNHKIjnbH90PPd5UNJHFQE5lD4TeHvBZEqlYz/QLqBMDcbUQmg3xjYVLLT3X2rEMwqGkxs8MoTnEUs3e9Zwe7UJ8JNKRZlrCveWOXdlDpBEFpN/CcmxyB9gP+/8N7hjtEAFuOwn8QwIDAQAB";
        _channelAdapterHuawei.pay_priv_key ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCwbbPK+KOj1PmRrezj8blAImGzhefAmX6cD0AZ1FvWw7eH6i9X/czCskbpM188iG4Mrb9X0Xlv6t5++nmMTwTRiGBVhVDu/lkYp/LYOpFaT8w7L4eSXWtb1x9T3vi2/pxFnrh3jFr4/yD70KQz3cTIllgSTLWgsuJ3CYAgbPYooupHnopIyitVR8fgv+YXtneFWsM79gAENGRi1gZWtQFou8tSH0P4QR+DNeJBAW8xTHc1IgmHGrhoyG1tHsYwAembsgsO0AfXusLqO5FIdWbwmoQ99O9mt6ffLrf6EMg99xo6AaUk0eX0flDMFSP1u1XtoIxqpM+5ECxl3G1cj/WdAgMBAAECggEABuBLrtg4doDtNiiPcEXiSLXeVXMdWHQTC22Lu8X7S1hG7ajqrfUCFz+ABIRATLlrgpp99+P95ecDQ3MrLBUDVNBKIuMmh54JpmybSiKkOh8ckFz2nXV/Iu30Nl5dFrK/L/Zy7ZPektBMvou/7paQ3hl/9ryfIpN+QJHx+LGOeYBpR1mYdIr9Rzs6nnzoZYQIfav5sPh8e9IL5uKXmhVZsLoOIK3H8Poz90q8JRaGRQ2E+9vo+MzknM9phldsRhL7MWUp3aVDAWcL5rTRq3rRIEJyaV/5CCuNjuqJZbFcca2Kp1WJ7yon0H4G/V5/6BMOzTV4oSN2NwkbxmSGJiSeyQKBgQDyrEYsVwsRwjqWmp9agOEwsUW1wQFKmhcRuXmNyQW6eoWEN3cfioNS9SD8/FTYOpa3r3cEectxPpglPUK0ojLwMCAcZXOnK0WKPA1pugV9R/DmyAfsSraWQsiJ/r5NYN0TqgW0igzFb/hMl4obHffsvd7tYmzXdyPkJZ1018VElQKBgQC6Hhn4pyjUjBw3V0NPViUUi4XLaYyN5SNJ/7sVsFeguXgQqI9GQPn3ADfHvqGURmKOv+89aFPfPG1V6twh1z95q62rIQRQkjGW0Qlb+CA5ITN1I01DPThlLT1uh1xjdQtcCrcu/9aLETSPbaW98TLy5Okzh5h55iJsO04TVlXi6QKBgHjQ9A7XNxecT3bzfCqex35qg349nNr7ZEjawn2oe0WgAiEVTCVT3JXMnKPXLJaKdMb2gJal3HeA5laD7VXo+ttB2nrIt4+ep3xu8W6Vwm3hBzqek/M8LzMYBKdPQIddljNYDocXw8v4NvJK7+MYRHqE/RvKSM3s7t2DLKUjV38RAoGBAJ4FDrHyYxVLPG20Nl722ha+e0OAgyi6gqLCcLpydYPt1MX4B2Vzp4eSdMmvzgpRVD61MU/m80cXTBFlja9hzO/l1lb0enBaH6ZYJuUiH2HCEamD5oaPRQGKTp04ajW7W4CzTGKoLyT137VjOBSz5DkcJ+9rUgc1YMaZ96ktZVB5AoGBAMv6qMTqEQ3P3HXotGR8GOtN8c8tl/xZ5k2E9G0tybiHm3q+vrLYnPCwAPZxU33e3Z+AMlmqs44WsbrNKF3oThWJ/9R8EtFBujgJ7SsB/C8PSsDsC3Kg3D0q7+h9KuhbAgeKq3Ep42NE3myTz+RXmnAA1v8mY6k2bIgTKwz6cyqd";
        _channelAdapterHuawei.pay_pub_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsG2zyvijo9T5ka3s4/G5QCJhs4XnwJl+nA9AGdRb1sO3h+ovV/3MwrJG6TNfPIhuDK2/V9F5b+refvp5jE8E0YhgVYVQ7v5ZGKfy2DqRWk/MOy+Hkl1rW9cfU974tv6cRZ64d4xa+P8g+9CkM93EyJZYEky1oLLidwmAIGz2KKLqR56KSMorVUfH4L/mF7Z3hVrDO/YABDRkYtYGVrUBaLvLUh9D+EEfgzXiQQFvMUx3NSIJhxq4aMhtbR7GMAHpm7ILDtAH17rC6juRSHVm8JqEPfTvZren3y63+hDIPfcaOgGlJNHl9H5QzBUj9btV7aCMaqTPuRAsZdxtXI/1nQIDAQAB";
        _channelAdapterHuawei.appId = "100583027";
        _channelAdapterHuawei.cpId = "900086000021108251";
        _channelAdapterHuawei.init(_activity.getApplication());

    }

    @Override
    public void initSdk(Activity activity) {
        _channelAdapterHuawei.initHMSAgent(activity);
    }

    @Override
    public void login(final Activity activity) {
        _channelAdapterHuawei.login(activity, new ChannelAdapterMain.LoginCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(_activity,"登陆成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(_activity,"登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void pay(String productId) {
        ChannelAdapterMain.ProductInfo productInfo = new ChannelAdapterMain.ProductInfo();
        productInfo.productId = productId;
        productInfo.amount = productId;
        productInfo.description = "去除所有广告";
        productInfo.title = "去广告";
        _channelAdapterHuawei.pay(_activity, productInfo, new ChannelAdapterMain.PayCallback(){

            @Override
            public void onSuccess(ChannelAdapterMain.ProductInfo productInfo1) {
                Toast.makeText(_activity, "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(ChannelAdapterMain.ProductInfo productInfo1, String error) {
                Toast.makeText(_activity, "支付失败：" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void exitGame(Activity activity) {
        _channelAdapterHuawei.checkLostPay(activity, new ChannelAdapterMain.CheckPayCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(_activity, "检查漏单成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(_activity, "检查漏单失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume(Activity activity) {
        _channelAdapterHuawei.onResume(activity);
    }

    @Override
    public void onPause(Activity activity) {
        _channelAdapterHuawei.onPause(activity);
    }

    @Override
    public void onStart(Activity activity) {

    }

    @Override
    public void onRestart(Activity activity) {

    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestroy(Activity activity) {

    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }
}
