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

// var cbc_init = Module.findGlobalExportByName("$s11CryptoSwift3CBCV2ivACSays5UInt8VG_tcfC");
// Interceptor.attach(cbc_init, {
//   onEnter(args) {
//     console.log(`[+] CryptoSwift.CBC.init( iv: ${iv})`);
//     // console.log(hexdump(ptr(args[0]), { length: 64 }))
//     console.log("\n\n")
//   },
//   onLeave(retval) {
//     console.log(hexdump(ptr(retval), { length: 64 }))
//     console.log(`\t return ${retval}`);
//   }
// });

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
