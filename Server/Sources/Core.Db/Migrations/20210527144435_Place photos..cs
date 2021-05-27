using Microsoft.EntityFrameworkCore.Migrations;
using Npgsql.EntityFrameworkCore.PostgreSQL.Metadata;

namespace Core.Db.Migrations
{
    public partial class Placephotos : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropForeignKey(
                name: "FK_Photos_Places_PlaceId",
                schema: "citylogia",
                table: "Photos");

            migrationBuilder.DropIndex(
                name: "IX_Photos_PlaceId",
                schema: "citylogia",
                table: "Photos");

            migrationBuilder.DropColumn(
                name: "PlaceId",
                schema: "citylogia",
                table: "Photos");

            migrationBuilder.CreateTable(
                name: "Place-Photo",
                schema: "citylogia",
                columns: table => new
                {
                    Id = table.Column<long>(type: "bigint", nullable: false)
                        .Annotation("Npgsql:ValueGenerationStrategy", NpgsqlValueGenerationStrategy.IdentityByDefaultColumn),
                    PhotoId = table.Column<long>(type: "bigint", nullable: false),
                    IsMain = table.Column<bool>(type: "boolean", nullable: false),
                    PlaceId = table.Column<long>(type: "bigint", nullable: false),
                    Deleted = table.Column<bool>(type: "boolean", nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_Place-Photo", x => x.Id);
                    table.ForeignKey(
                        name: "FK_Place-Photo_Photos_PhotoId",
                        column: x => x.PhotoId,
                        principalSchema: "citylogia",
                        principalTable: "Photos",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_Place-Photo_Places_PlaceId",
                        column: x => x.PlaceId,
                        principalSchema: "citylogia",
                        principalTable: "Places",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_Place-Photo_PhotoId",
                schema: "citylogia",
                table: "Place-Photo",
                column: "PhotoId",
                unique: true);

            migrationBuilder.CreateIndex(
                name: "IX_Place-Photo_PlaceId",
                schema: "citylogia",
                table: "Place-Photo",
                column: "PlaceId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "Place-Photo",
                schema: "citylogia");

            migrationBuilder.AddColumn<long>(
                name: "PlaceId",
                schema: "citylogia",
                table: "Photos",
                type: "bigint",
                nullable: false,
                defaultValue: 0L);

            migrationBuilder.CreateIndex(
                name: "IX_Photos_PlaceId",
                schema: "citylogia",
                table: "Photos",
                column: "PlaceId");

            migrationBuilder.AddForeignKey(
                name: "FK_Photos_Places_PlaceId",
                schema: "citylogia",
                table: "Photos",
                column: "PlaceId",
                principalSchema: "citylogia",
                principalTable: "Places",
                principalColumn: "Id",
                onDelete: ReferentialAction.Cascade);
        }
    }
}
