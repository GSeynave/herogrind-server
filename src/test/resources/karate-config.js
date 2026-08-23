function fn() {
  karate.configure('logging', {
    report: 'info',
    console: 'info',
    pretty: true
  });

  return {
    baseUrl: 'http://localhost:8080'
  };
}