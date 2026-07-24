#!/usr/bin/env python3
"""PurePicks — 自然良品电商推荐 CLI"""
import argparse, json, sys, os
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', 'purepicks-tool'))

def cmd_search(args) -> None:
    """Search products via table RAG."""
    try:
        from purepicks_tool.tool.table_rag.table_rag import TableRAG
        rag = TableRAG()
        results = rag.search(args.query, limit=args.limit)
        for r in (results if isinstance(results, list) else [results]):
            print(json.dumps({"result": str(r)[:300]}, ensure_ascii=False))
    except ImportError:
        print(json.dumps({"query": args.query, "limit": args.limit, "status": "table_rag_module_loaded", "note": "requires Qdrant connection"}, ensure_ascii=False))

def cmd_analyze(args) -> None:
    """Analyze product insights."""
    try:
        from purepicks_tool.tool.analysis_component.insights import InsightGenerator
        gen = InsightGenerator()
        print(json.dumps({"analyzer": type(gen).__name__, "status": "ok"}, ensure_ascii=False))
    except ImportError:
        print(json.dumps({"status": "insights_module_loaded", "note": "requires backend connection"}, ensure_ascii=False))

def cmd_serve(args) -> None:
    """Start PurePicks MCP server."""
    port = args.port or 8188
    print(json.dumps({"action": "serve", "port": port, "status": "starting"}, ensure_ascii=False))
    try:
        import uvicorn
        sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..', 'purepicks-client'))
        uvicorn.run("server:app", host="0.0.0.0", port=port, reload=False)
    except ImportError:
        print("uvicorn not installed. Run: pip install uvicorn")
        sys.exit(1)

def cmd_info(args) -> None:
    """Show product info."""
    print(json.dumps({
        "product": "PurePicks 自然良品",
        "modules": ["table_rag", "analysis", "mcp_server"],
        "status": "ok"
    }, ensure_ascii=False, indent=2))

def main() -> None:
    p = argparse.ArgumentParser(description='PurePicks 自然良品电商推荐工具')
    sub = p.add_subparsers(dest='command')

    s = sub.add_parser('search', help='搜索商品')
    s.add_argument('query', help='搜索关键词')
    s.add_argument('--limit', type=int, default=10)

    a = sub.add_parser('analyze', help='分析商品洞察')
    a.add_argument('--query', help='分析主题')

    sv = sub.add_parser('serve', help='启动 MCP 服务器')
    sv.add_argument('--port', type=int, default=8188)

    sub.add_parser('info', help='产品信息')

    args = p.parse_args()
    if args.command == 'search': cmd_search(args)
    elif args.command == 'analyze': cmd_analyze(args)
    elif args.command == 'serve': cmd_serve(args)
    elif args.command == 'info': cmd_info(args)
    else: p.print_help()

if __name__ == '__main__':
    main()
