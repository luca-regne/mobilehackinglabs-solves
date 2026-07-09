var fn = Module.findGlobalExportByName("$s9No_Escape12isJailbrokenSbyF");
Interceptor.attach(fn, {
    onEnter(args) {
        console.log("[*] No_Escape.isJailbroken()");
    },
    onLeave(retval){
      retval.replace(0);
      console.log(`\tReplace return: ${retval}`)
    }
});
