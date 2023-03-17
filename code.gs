const CLIENT_ID = 'YOUR_CLIENT_ID';
const CLIENT_SECRET = 'YOUR_CLIENT_SECRET';
const SLACK_VERIFICATION_TOKEN = 'YOUR_SLACK_VERIFICATION_TOKEN';

// OAuth2サービスをセットアップ
function getOAuthService() {
  return OAuth2.createService('slack')
    .setAuthorizationBaseUrl('https://accounts.google.com/o/oauth2/auth')
    .setTokenUrl('https://accounts.google.com/o/oauth2/token')
    .setClientId(CLIENT_ID)
    .setClientSecret(CLIENT_SECRET)
    .setPropertyStore(PropertiesService.getUserProperties())
    .setScope('https://www.googleapis.com/auth/userinfo.email')
    .setCallbackFunction('authCallback');
}

// 認証フロー開始
function doGet() {
  const oauthService = getOAuthService();
  if (!oauthService.hasAccess()) {
    const authorizationUrl = oauthService.getAuthorizationUrl();
    return HtmlService.createHtmlOutput(`<a href="${authorizationUrl}">認証を行ってください</a>`);
  } else {
    return HtmlService.createHtmlOutput('認証済みです');
  }
}

// 認証コールバック
function authCallback(request) {
  const oauthService = getOAuthService();
  const authorized = oauthService.handleCallback(request);
  if (authorized) {
    return HtmlService.createHtmlOutput('認証に成功しました');
  } else {
    return HtmlService.createHtmlOutput('認証に失敗しました');
  }
}

// doPost関数
function doPost(e) {
  const oauthService = getOAuthService();
  if (!oauthService.hasAccess()) {
    return ContentService.createTextOutput('認証が必要です');
  }

  if (e.parameter.token !== SLACK_VERIFICATION_TOKEN) {
    return ContentService.createTextOutput('不正なリクエストです');
  }

  const response = {
    response_type: 'in_channel',
    text: 'Hello, Slack!',
  };

  return ContentService.createTextOutput(JSON.stringify(response)).setMimeType(ContentService.MimeType.JSON);
}
