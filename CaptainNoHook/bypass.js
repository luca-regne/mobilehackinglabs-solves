var is_noncompliant_device = Module.findGlobalExportByName("$s14Captain_Nohook22is_noncompliant_deviceSbyF");

Interceptor.attach(is_noncompliant_device, {
  onEnter(args) {
    console.log(`Captain_Nohook.is_noncompliant_device`);
  },
  onLeave(retval) {
    retval.replace(0x00);
    console.log(`\t return ${retval}`);
  }
});

var decrypt = Module.findGlobalExportByName("$s11CryptoSwift6CipherPAAE7decryptySays5UInt8VGAGKF");

Interceptor.attach(decrypt, {
  onEnter(args) {
    console.log(`[+] CryptoSwift.Cipher.decrypt()`);
  },
  onLeave(retval) {
    const str = retval.readCString();
    console.log(hexdump(ptr(retval), { length: 64 }))
    console.log("\n\n")
  }
});
