package com.example.myapp;

import android.os.AsyncTask;

import java.net.URL;

private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
    protected Long doInBackground(URL... urls) {
        int count = urls.length;
        long totalSize = 0;
        for (int i = 0; i < count; i++) {
            totalSize += Downloader.downloadFile(urls[i]);
            publishProgress((int) ((i / (float) count) * 100));              // Escape early if cancel() is called
            if (isCancelled())
                break;
        }
        return totalSize;
    }

    protected void onProgressUpdate(Integer... progress) {
        setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        showDialog("Downloaded " + result + " bytes");
    }

    public String executeHttpGet() {
        String result = null;
        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader in = null;
        try {
            url = new URL("http://10.0.2.2:8888/data/get/?token=alexzhou");
            connection = (HttpURLConnection) url.openConnection();
            in = new InputStreamReader(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            StringBuffer strBuffer = new StringBuffer();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                strBuffer.append(line);
            }
            result = strBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return result;
    }

    private String doVote(String voteStr) {
        String retStr = "";
        Log.i("vote", "doVote() voteStr:" + voteStr);

        try {

            StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
            stringBuffer.append("r=").append(URLEncoder.encode(voteStr, "utf-8"));

            byte[] data = stringBuffer.toString().getBytes();
            String urlPath = "http://192.168.1.102:8080/vote/GetVote";
            URL url = new URL(urlPath);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);     //设置连接超时时间
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存             //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");             //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));             //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if (response == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                retStr = inputStreamToString(inputStream);                     //处理服务器的响应结果
                Log.i("vote", "retStr:" + retStr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retStr;
    }

    private class VoteTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            for (String p: params ) {
                Log.i(TAG, "doInBackground: " + p);
            }
            String ret = doVote(params[0]);
            return ret;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(com.example.myerc.MainActivity6.this, s, Toast.LENGTH_SHORT).show();
        }
        public void onClick(View btn) {

            switch(btn.getId()){
                case R.id.btn_approve:
                    new VoteTask().execute("赞成");
                    break;
                case R.id.btn_object:
                    new VoteTask().execute("反对");
                    break;
                case R.id.btn_abstain:
                    new VoteTask().execute("弃权");
                    break;        
            }
        }
    }
